package com.product.vexora.service;

import com.product.vexora.dto.FaturamentoDTO;
import com.product.vexora.dto.ProdutoMaisVendidoDto;

import java.util.List;


public interface RelatorioService {

    FaturamentoDTO faturamentoSemanal();
    FaturamentoDTO faturamentoMensal();

    List<ProdutoMaisVendidoDto> produtosMaisVendidosSemana();
    List<ProdutoMaisVendidoDto> produtosMaisVendidosMes();
}
