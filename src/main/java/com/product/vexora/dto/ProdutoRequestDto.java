package com.product.vexora.dto;

import com.product.vexora.enums.CategoriaProduto;
import com.product.vexora.enums.UnidadeMedida;

import java.math.BigDecimal;

public record ProdutoRequestDto(
        String nome,
        CategoriaProduto categoria,
        UnidadeMedida unidade,
        BigDecimal precoCompra,
        BigDecimal precoVenda,
        BigDecimal estoqueAtual,
        BigDecimal estoqueMinimo
) {}
