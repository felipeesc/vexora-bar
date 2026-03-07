package com.product.vexora.service.impl;

import com.product.vexora.dto.*;
import com.product.vexora.dto.response.CategoriaResponse;
import com.product.vexora.entity.Comanda;
import com.product.vexora.entity.ComandaItem;
import com.product.vexora.entity.Pagamento;
import com.product.vexora.entity.Produto;
import com.product.vexora.exception.*;
import com.product.vexora.repository.ComandaItemRepository;
import com.product.vexora.repository.ComandaRepository;
import com.product.vexora.repository.ProdutoRepository;
import com.product.vexora.service.ComandaService;
import com.product.vexora.service.MovimentacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComandaServiceImpl implements ComandaService {

    private final MovimentacaoService movimentacaoService;
    private final ComandaRepository comandaRepository;
    private final ComandaItemRepository itemRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    @Transactional
    public ComandaResponseDTO abrirComanda(ComandaRequestDTO dto) {

        if (dto.mesa() == null) {
            throw new MesaObrigatoriaException();
        }

        String identificador = dto.identificador();
        if (identificador == null || identificador.isBlank()) {
            identificador = gerarIdentificadorAutomatico(dto.mesa());
        }

        Comanda comanda = new Comanda();
        comanda.setMesa(dto.mesa());
        comanda.setCliente(dto.cliente());
        comanda.setIdentificador(identificador);
        comanda.setAberta(true);
        comanda.setAbertura(LocalDateTime.now());

        saveComanda(comanda);

        return toResponse(comanda);
    }



    private Comanda saveComanda(Comanda comanda) {
        try {
            return comandaRepository.save(comanda);
        } catch (DataIntegrityViolationException ex) {
            throw new ComandaAbertaException(
                    comanda.getMesa(),
                    comanda.getIdentificador()
            );
        }
    }


    @Override
    public ComandaResponseDTO buscarPorId(UUID id) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(ComandaNaoEncontradaException::new);

        return toResponse(comanda);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComandaResponseDTO> listar(
            Boolean aberta,
            Integer mesa,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        return comandaRepository.filtrar(aberta, mesa, inicio, fim)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ComandaResponseDTO adicionarItem(ComandaItemRequestDTO dto) {

        Comanda comanda = comandaRepository.findById(dto.comandaId())
                .orElseThrow(ComandaNaoEncontradaException::new);

        if (!comanda.isAberta()) {
            throw new ComandaFechadaException();
        }

        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new ProdutoNotFoundException(dto.produtoId()));

        ComandaItem item = new ComandaItem();
        item.setComanda(comanda);
        item.setProduto(produto);
        item.setDataHora(LocalDateTime.now());
        item.setQuantidade(dto.quantidade());

        itemRepository.save(item);
        comanda.getItens().add(item);

        movimentacaoService.registrarSaidaPorComanda(
                produto,
                dto.quantidade(),
                comanda.getId()
        );

        return toResponse(comanda);
    }

    @Override
    @Transactional
    public ComandaResponseDTO cancelarItem(UUID itemId, CancelItemRequestDTO dto) {

        ComandaItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNaoEncontradoException(itemId));

        Comanda comanda = item.getComanda();

        if (!comanda.isAberta()) {
            throw new ComandaFechadaException();
        }

        if (item.isCancelado()) {
            throw new PagamentoInvalidoException("Este item já foi cancelado");
        }

        Produto produto = item.getProduto();
        int quantidade = item.getQuantidade();

        item.setCancelado(true);
        item.setMotivoCancelamento(dto.motivo());
        item.setDataCancelamento(LocalDateTime.now());
        itemRepository.save(item);

        movimentacaoService.registrarEntradaPorCancelamento(
                produto,
                quantidade,
                comanda.getId()
        );

        return toResponse(comanda);
    }


    @Override
    @Transactional
    public ComandaResponseDTO fecharComanda(UUID comandaId, FechamentoRequestDTO dto) {

        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(ComandaNaoEncontradaException::new);

        if (!comanda.isAberta()) {
            throw new ComandaFechadaException();
        }

        ComandaResponseDTO preview = toResponse(comanda);
        BigDecimal totalComanda = preview.total();

        validarPagamentos(dto.pagamentos(), totalComanda);

        for (PagamentoDTO pag : dto.pagamentos()) {
            comanda.getPagamentos().add(new Pagamento(comanda, pag.metodo(), pag.valor()));
        }

        comanda.setAberta(false);
        comanda.setFechamento(LocalDateTime.now());

        saveComanda(comanda);

        return toResponse(comanda);
    }

    @Override
    @Transactional
    public List<ComandaResponseDTO> fecharMesa(Integer mesa, FechamentoMesaRequestDTO dto) {

        List<Comanda> abertas = comandaRepository.filtrar(true, mesa, null, null);

        if (abertas.isEmpty()) {
            throw new ComandaNaoEncontradaException();
        }

        // Calcular total consolidado de todas as comandas da mesa
        BigDecimal totalMesa = BigDecimal.ZERO;
        for (Comanda c : abertas) {
            ComandaResponseDTO preview = toResponse(c);
            totalMesa = totalMesa.add(preview.total());
        }

        validarPagamentos(dto.pagamentos(), totalMesa);

        LocalDateTime agora = LocalDateTime.now();

        // Distribuir pagamentos proporcionalmente entre as comandas
        List<ComandaResponseDTO> resultado = new ArrayList<>();
        for (int i = 0; i < abertas.size(); i++) {
            Comanda comanda = abertas.get(i);

            // A primeira comanda recebe todos os pagamentos (referência contábil)
            if (i == 0) {
                for (PagamentoDTO pag : dto.pagamentos()) {
                    comanda.getPagamentos().add(new Pagamento(comanda, pag.metodo(), pag.valor()));
                }
            }

            comanda.setAberta(false);
            comanda.setFechamento(agora);
            saveComanda(comanda);
            resultado.add(toResponse(comanda));
        }

        return resultado;
    }

    private void validarPagamentos(List<PagamentoDTO> pagamentos, BigDecimal totalEsperado) {
        if (pagamentos == null || pagamentos.isEmpty()) {
            throw new PagamentoInvalidoException("É necessário informar pelo menos um pagamento");
        }

        BigDecimal totalPago = pagamentos.stream()
                .map(PagamentoDTO::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPago.compareTo(totalEsperado) != 0) {
            throw new PagamentoInvalidoException(
                    String.format("O valor pago (R$ %s) não corresponde ao total da conta (R$ %s)",
                            totalPago.setScale(2, java.math.RoundingMode.HALF_UP),
                            totalEsperado.setScale(2, java.math.RoundingMode.HALF_UP))
            );
        }

        for (PagamentoDTO pag : pagamentos) {
            if (pag.valor() == null || pag.valor().compareTo(BigDecimal.ZERO) <= 0) {
                throw new PagamentoInvalidoException("Cada pagamento deve ter um valor positivo");
            }
            if (pag.metodo() == null) {
                throw new PagamentoInvalidoException("Cada pagamento deve ter um método definido");
            }
        }
    }

    @Override
    @Transactional
    public ComandaResponseDTO dividirComanda(UUID comandaId, SplitComandaRequestDTO dto) {

        Comanda origem = comandaRepository.findById(comandaId)
                .orElseThrow(ComandaNaoEncontradaException::new);

        if (!origem.isAberta()) {
            throw new ComandaFechadaException();
        }

        // Validar que todos os items pertencem à comanda de origem e não estão cancelados
        List<ComandaItem> itensParaMover = new ArrayList<>();
        for (UUID itemId : dto.itemIds()) {
            ComandaItem item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ItemNaoEncontradoException(itemId));
            if (!item.getComanda().getId().equals(comandaId)) {
                throw new PagamentoInvalidoException("Item " + itemId + " não pertence a esta comanda");
            }
            if (item.isCancelado()) {
                throw new PagamentoInvalidoException("Não é possível dividir itens cancelados");
            }
            itensParaMover.add(item);
        }

        // Criar nova comanda na mesma mesa
        Comanda nova = new Comanda();
        nova.setMesa(origem.getMesa());
        nova.setCliente(dto.cliente());
        nova.setIdentificador(gerarIdentificadorAutomatico(origem.getMesa()));
        nova.setAberta(true);
        nova.setAbertura(LocalDateTime.now());
        saveComanda(nova);

        // Mover itens
        for (ComandaItem item : itensParaMover) {
            item.setComanda(nova);
            itemRepository.save(item);
        }

        return toResponse(nova);
    }

    @Override
    @Transactional
    public ComandaResponseDTO transferirItens(UUID comandaOrigemId, TransferItemRequestDTO dto) {

        Comanda origem = comandaRepository.findById(comandaOrigemId)
                .orElseThrow(ComandaNaoEncontradaException::new);

        Comanda destino = comandaRepository.findById(dto.comandaDestinoId())
                .orElseThrow(ComandaNaoEncontradaException::new);

        if (!origem.isAberta() || !destino.isAberta()) {
            throw new ComandaFechadaException();
        }

        if (!origem.getMesa().equals(destino.getMesa())) {
            throw new PagamentoInvalidoException("Só é possível transferir itens entre comandas da mesma mesa");
        }

        for (UUID itemId : dto.itemIds()) {
            ComandaItem item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ItemNaoEncontradoException(itemId));
            if (!item.getComanda().getId().equals(comandaOrigemId)) {
                throw new PagamentoInvalidoException("Item " + itemId + " não pertence à comanda de origem");
            }
            if (item.isCancelado()) {
                throw new PagamentoInvalidoException("Não é possível transferir itens cancelados");
            }
            item.setComanda(destino);
            itemRepository.save(item);
        }

        return toResponse(destino);
    }

    @Override
    @Transactional
    public ComandaResponseDTO juntarComandas(MergeComandaRequestDTO dto) {

        List<Comanda> comandas = new ArrayList<>();
        Integer mesa = null;

        for (UUID id : dto.comandaIds()) {
            Comanda c = comandaRepository.findById(id)
                    .orElseThrow(ComandaNaoEncontradaException::new);
            if (!c.isAberta()) {
                throw new ComandaFechadaException();
            }
            if (mesa == null) {
                mesa = c.getMesa();
            } else if (!mesa.equals(c.getMesa())) {
                throw new PagamentoInvalidoException("Só é possível juntar comandas da mesma mesa");
            }
            comandas.add(c);
        }

        Comanda destino = comandas.get(0);

        for (int i = 1; i < comandas.size(); i++) {
            Comanda origem = comandas.get(i);

            // Mover todos itens não cancelados para o destino
            List<ComandaItem> itens = new ArrayList<>(origem.getItens());
            for (ComandaItem item : itens) {
                item.setComanda(destino);
                itemRepository.save(item);
            }

            // Fechar a comanda de origem (vazia)
            origem.setAberta(false);
            origem.setFechamento(LocalDateTime.now());
            saveComanda(origem);
        }

        return toResponse(destino);
    }

    @Override
    @Transactional
    public ComandaResponseDTO reabrirComanda(UUID comandaId) {

        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(ComandaNaoEncontradaException::new);

        if (comanda.isAberta()) {
            throw new ComandaAbertaException(comanda.getMesa(), comanda.getIdentificador());
        }

        comanda.setAberta(true);
        comanda.setFechamento(null);

        // Remover pagamentos associados
        comanda.getPagamentos().clear();

        saveComanda(comanda);

        return toResponse(comanda);
    }

    @Override
    public ComandaResponseDTO calcular(UUID comandaId) {

        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(ComandaNaoEncontradaException::new);

        return toResponse(comanda);
    }


    public List<ComandaResponseDTO> listarAbertas() {
        return comandaRepository.findByAbertaTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ComandaItemDTO toItemDTO(ComandaItem item) {
        Produto produto = item.getProduto();

        BigDecimal totalItem = item.isCancelado()
                ? BigDecimal.ZERO
                : produto.getPrecoVenda().multiply(BigDecimal.valueOf(item.getQuantidade()));

        CategoriaResponse categoriaResponse = new CategoriaResponse(
                produto.getCategoria().getId(),
                produto.getCategoria().getNome(),
                produto.getCategoria().getDescricao(),
                produto.getCategoria().isAtiva(),
                produto.getCategoria().getCriadoEm()
        );

        return new ComandaItemDTO(
                item.getId(),
                produto.getNome(),
                categoriaResponse,
                produto.getUnidade(),
                item.getQuantidade(),
                item.getDataHora(),
                produto.getPrecoVenda(),
                totalItem,
                item.isCancelado(),
                item.getMotivoCancelamento()
        );
    }


    private ComandaResponseDTO toResponse(Comanda comanda) {

        List<ComandaItemDTO> itens = comanda.getItens()
                .stream()
                .map(this::toItemDTO)
                .toList();

        // Total exclui itens cancelados
        BigDecimal total = itens.stream()
                .filter(i -> !i.cancelado())
                .map(ComandaItemDTO::totalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<PagamentoResponseDTO> pagamentos = comanda.getPagamentos()
                .stream()
                .map(p -> new PagamentoResponseDTO(p.getId(), p.getMetodo(), p.getValor()))
                .toList();

        return new ComandaResponseDTO(
                comanda.getId(),
                comanda.getMesa(),
                comanda.getCliente(),
                comanda.isAberta(),
                comanda.getAbertura(),
                comanda.getFechamento(),
                itens,
                total,
                pagamentos
        );
    }

    private String gerarIdentificadorAutomatico(Integer mesa) {
        long total = comandaRepository.countByMesaAndAbertaTrue(mesa);

        return "COMANDA-" + (total + 1);
    }
}
