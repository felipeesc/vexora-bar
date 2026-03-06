package com.product.vexora.service;

import com.product.vexora.dto.FaturamentoDTO;
import com.product.vexora.dto.ProdutoMaisVendidoDto;
import com.product.vexora.dto.RelatorioEstoqueDTO;
import com.product.vexora.dto.MovimentacaoResponseDTO;

import java.time.LocalDate;
import java.util.List;


public interface RelatorioService {

    FaturamentoDTO faturamentoDiario(LocalDate data);
    FaturamentoDTO faturamentoSemanal(LocalDate referencia);
    FaturamentoDTO faturamentoMensal(LocalDate referencia);

    List<ProdutoMaisVendidoDto> produtosMaisVendidosDia(LocalDate data);
    List<ProdutoMaisVendidoDto> produtosMaisVendidosSemana(LocalDate referencia);
    List<ProdutoMaisVendidoDto> produtosMaisVendidosMes(LocalDate referencia);

    RelatorioEstoqueDTO relatorioEstoque();
    List<MovimentacaoResponseDTO> historicoMovimentacoes(LocalDate inicio, LocalDate fim);
}
