package com.product.vexora.service.impl;

import com.product.vexora.dto.FaturamentoDTO;
import com.product.vexora.dto.ProdutoMaisVendidoDto;
import com.product.vexora.repository.MovimentacaoRepository;
import com.product.vexora.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioServiceImpl implements RelatorioService {

    private final MovimentacaoRepository movimentacaoRepository;

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
}
