package com.product.vexora.service.impl;

import com.product.vexora.dto.ComandaItemDTO;
import com.product.vexora.dto.ComandaItemRequestDTO;
import com.product.vexora.dto.ComandaRequestDTO;
import com.product.vexora.dto.ComandaResponseDTO;
import com.product.vexora.entity.Comanda;
import com.product.vexora.entity.ComandaItem;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComandaServiceImpl implements ComandaService {

    private final MovimentacaoService movimentacaoService;
    private final ComandaRepository comandaRepository;
    private final ComandaItemRepository itemRepository;
    private final ProdutoRepository produtoRepository;

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
    public List<ComandaResponseDTO> listar(
            Boolean aberta,
            Integer mesa,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {

        List<Comanda> comandas = comandaRepository.findAll();

        return comandas.stream()
                .filter(c -> aberta == null || c.isAberta() == aberta)
                .filter(c -> mesa == null || c.getMesa().equals(mesa))
                .filter(c -> {
                    if (inicio == null && fim == null) return true;
                    LocalDateTime abertura = c.getAbertura();
                    if (inicio != null && abertura.isBefore(inicio)) return false;
                    if (fim != null && abertura.isAfter(fim)) return false;
                    return true;
                })
                .map(this::toResponse)
                .toList();
    }

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

    public ComandaResponseDTO removerItem(UUID itemId) {

        ComandaItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNaoEncontradoException(itemId));

        Comanda comanda = item.getComanda();

        if (!comanda.isAberta()) {
            throw new ComandaFechadaException();
        }

        Produto produto = item.getProduto();
        int quantidade = item.getQuantidade();

        itemRepository.delete(item);
        comanda.getItens().remove(item);

        movimentacaoService.registrarEntradaPorCancelamento(
                produto,
                quantidade,
                comanda.getId()
        );

        return toResponse(comanda);
    }


    public ComandaResponseDTO fecharComanda(UUID comandaId) {

        Comanda comanda = comandaRepository.findById(comandaId)
                .orElseThrow(ComandaNaoEncontradaException::new);

        if (!comanda.isAberta()) {
            throw new ComandaFechadaException();
        }

        comanda.setAberta(false);
        comanda.setFechamento(LocalDateTime.now());

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

        BigDecimal totalItem = produto.getPrecoVenda()
                .multiply(BigDecimal.valueOf(item.getQuantidade()));

        return new ComandaItemDTO(
                item.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getUnidade(),
                item.getQuantidade(),
                item.getDataHora(),
                produto.getPrecoVenda(),
                totalItem
        );
    }


    private ComandaResponseDTO toResponse(Comanda comanda) {

        List<ComandaItemDTO> itens = comanda.getItens()
                .stream()
                .map(this::toItemDTO)
                .toList();

        BigDecimal total = itens.stream()
                .map(ComandaItemDTO::totalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ComandaResponseDTO(
                comanda.getId(),
                comanda.getMesa(),
                comanda.getCliente(),
                comanda.isAberta(),
                itens,
                total
        );
    }

    private String gerarIdentificadorAutomatico(Integer mesa) {
        long total = comandaRepository.countByMesaAndAbertaTrue(mesa);

        return "COMANDA-" + (total + 1);
    }
}
