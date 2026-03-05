package com.product.vexora.service.impl;

import com.product.vexora.dto.*;
import com.product.vexora.entity.Produto;
import com.product.vexora.repository.MovimentacaoRepository;
import com.product.vexora.repository.ProdutoRepository;
import com.product.vexora.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RelatorioServiceImpl implements RelatorioService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    public FaturamentoDTO faturamentoDiario(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(LocalTime.MAX);
        return movimentacaoRepository.faturamentoPeriodo(inicio, fim);
    }

    @Override
    public FaturamentoDTO faturamentoSemanal() {
        LocalDateTime fim = LocalDateTime.now();
        LocalDateTime inicio = fim.minusWeeks(1);
        return movimentacaoRepository.faturamentoPeriodo(inicio, fim);
    }

    @Override
    public FaturamentoDTO faturamentoMensal() {
        LocalDateTime fim = LocalDateTime.now();
        LocalDateTime inicio = fim.minusMonths(1);
        return movimentacaoRepository.faturamentoPeriodo(inicio, fim);
    }

    @Override
    public List<ProdutoMaisVendidoDto> produtosMaisVendidosDia(LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(LocalTime.MAX);
        return movimentacaoRepository.produtosMaisVendidos(inicio, fim);
    }

    @Override
    public List<ProdutoMaisVendidoDto> produtosMaisVendidosSemana() {
        LocalDateTime fim = LocalDateTime.now();
        LocalDateTime inicio = fim.minusWeeks(1);
        return movimentacaoRepository.produtosMaisVendidos(inicio, fim);
    }

    @Override
    public List<ProdutoMaisVendidoDto> produtosMaisVendidosMes() {
        LocalDateTime fim = LocalDateTime.now();
        LocalDateTime inicio = fim.minusMonths(1);
        return movimentacaoRepository.produtosMaisVendidos(inicio, fim);
    }

    @Override
    public RelatorioEstoqueDTO relatorioEstoque() {
        List<Produto> todos = produtoRepository.findAll();

        List<EstoqueProdutoDTO> produtos = todos.stream()
                .map(this::toEstoqueProdutoDTO)
                .toList();

        long abaixoDoMinimo = produtos.stream()
                .filter(EstoqueProdutoDTO::abaixoDoMinimo)
                .count();

        return new RelatorioEstoqueDTO(
                produtos.size(),
                abaixoDoMinimo,
                produtos
        );
    }

    @Override
    public List<MovimentacaoResponseDTO> historicoMovimentacoes(LocalDate inicio, LocalDate fim) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(LocalTime.MAX);

        return movimentacaoRepository.findByDataHoraBetween(inicioDateTime, fimDateTime)
                .stream()
                .map(m -> new MovimentacaoResponseDTO(
                        m.getId(),
                        m.getProduto().getNome(),
                        m.getTipo(),
                        m.getQuantidade(),
                        m.getMotivo(),
                        m.getDataHora()
                ))
                .toList();
    }

    private EstoqueProdutoDTO toEstoqueProdutoDTO(Produto produto) {
        boolean abaixo = produto.getEstoqueAtual().compareTo(produto.getEstoqueMinimo()) < 0;
        return new EstoqueProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getUnidade(),
                produto.getEstoqueAtual(),
                produto.getEstoqueMinimo(),
                abaixo
        );
    }
}
