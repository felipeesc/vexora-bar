package com.product.vexora.service.impl;

import com.product.vexora.dto.ComandaItemDTO;
import com.product.vexora.dto.ComandaItemRequestDTO;
import com.product.vexora.dto.ComandaRequestDTO;
import com.product.vexora.dto.ComandaResponseDTO;
import com.product.vexora.entity.Comanda;
import com.product.vexora.entity.ComandaItem;
import com.product.vexora.entity.Produto;
import com.product.vexora.exception.ComandaFechadaException;
import com.product.vexora.exception.ComandaNaoEncontradaException;
import com.product.vexora.exception.MesaObrigatoriaException;
import com.product.vexora.exception.ProdutoNotFoundException;
import com.product.vexora.repository.ComandaItemRepository;
import com.product.vexora.repository.ComandaRepository;
import com.product.vexora.repository.ProdutoRepository;
import com.product.vexora.service.ComandaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComandaServiceImpl implements ComandaService {

    private final ComandaRepository comandaRepository;
    private final ComandaItemRepository itemRepository;
    private final ProdutoRepository produtoRepository;

    public ComandaResponseDTO abrirComanda(ComandaRequestDTO dto) {

        if (dto.mesa() == null) {
            throw new MesaObrigatoriaException();
        }

        Comanda comanda = new Comanda();
        comanda.setMesa(dto.mesa());
        comanda.setCliente(dto.cliente());
        comanda.setAberta(true);
        comanda.setAbertura(LocalDateTime.now());

        comandaRepository.save(comanda);

        return toResponse(comanda);
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
        item.setQuantidade(dto.quantidade());

        itemRepository.save(item);
        comanda.getItens().add(item);

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

        comandaRepository.save(comanda);

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
}
