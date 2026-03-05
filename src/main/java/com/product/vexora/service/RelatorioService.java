package com.product.vexora.service;

import com.product.vexora.dto.FaturamentoDTO;
import com.product.vexora.dto.ProdutoMaisVendidoDto;
import com.product.vexora.dto.RelatorioEstoqueDTO;
import com.product.vexora.dto.MovimentacaoResponseDTO;

import java.time.LocalDate;
import java.util.List;


public interface RelatorioService {

    FaturamentoDTO faturamentoDiario(LocalDate data);
    FaturamentoDTO faturamentoSemanal();
    FaturamentoDTO faturamentoMensal();

    List<ProdutoMaisVendidoDto> produtosMaisVendidosDia(LocalDate data);
    List<ProdutoMaisVendidoDto> produtosMaisVendidosSemana();
    List<ProdutoMaisVendidoDto> produtosMaisVendidosMes();

    RelatorioEstoqueDTO relatorioEstoque();
    List<MovimentacaoResponseDTO> historicoMovimentacoes(LocalDate inicio, LocalDate fim);
}
