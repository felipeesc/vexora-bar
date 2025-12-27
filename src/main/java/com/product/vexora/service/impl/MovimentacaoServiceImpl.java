package com.product.vexora.service.impl;

import com.product.vexora.dto.MovimentacaoDto;
import com.product.vexora.entity.Movimentacao;
import com.product.vexora.entity.Produto;
import com.product.vexora.enums.TipoMovimentacao;
import com.product.vexora.exception.EstoqueInsuficienteException;
import com.product.vexora.exception.ProdutoNotFoundException;
import com.product.vexora.repository.MovimentacaoRepository;
import com.product.vexora.repository.ProdutoRepository;
import com.product.vexora.service.MovimentacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovimentacaoServiceImpl implements MovimentacaoService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    @Override
    public Movimentacao realizarMovimentacao(MovimentacaoDto dto) {

        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new ProdutoNotFoundException(dto.produtoId()));


        if (dto.tipo() == TipoMovimentacao.SAIDA &&
                produto.getEstoqueAtual().compareTo(dto.quantidade()) < 0) {
            throw new EstoqueInsuficienteException(produto.getNome());
        }

        atualizarEstoque(produto, dto);

        produtoRepository.save(produto);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(produto);
        movimentacao.setTipo(dto.tipo());
        movimentacao.setQuantidade(dto.quantidade());
        movimentacao.setMotivo(dto.motivo());
        movimentacao.setDataHora(LocalDateTime.now());

        return movimentacaoRepository.save(movimentacao);
    }

    private void atualizarEstoque(Produto produto, MovimentacaoDto dto) {
        if (dto.tipo() == TipoMovimentacao.ENTRADA) {
            produto.setEstoqueAtual(produto.getEstoqueAtual().add(dto.quantidade()));
        } else {
            produto.setEstoqueAtual(produto.getEstoqueAtual().subtract(dto.quantidade()));
        }
    }

    @Override
    public void registrarSaidaPorComanda(Produto produto, int quantidade, UUID comandaId) {

        MovimentacaoDto dto = new MovimentacaoDto(
                produto.getId(),
                TipoMovimentacao.SAIDA,
                BigDecimal.valueOf(quantidade),
                "Saída por comanda " + comandaId
        );

        realizarMovimentacao(dto);
    }

    @Override
    public void registrarEntradaPorCancelamento(Produto produto, int quantidade, UUID comandaId) {

        MovimentacaoDto dto = new MovimentacaoDto(
                produto.getId(),
                TipoMovimentacao.ENTRADA,
                BigDecimal.valueOf(quantidade),
                "Estorno de comanda " + comandaId
        );

        realizarMovimentacao(dto);
    }

}
