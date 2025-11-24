package com.product.vexora.dto;

import java.math.BigDecimal;

public record ProdutoMaisVendidoDto(
        String produto,
        BigDecimal quantidadeVendida,
        BigDecimal estoqueAtual
) {}
