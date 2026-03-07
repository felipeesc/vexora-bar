package com.product.vexora.dto;

import com.product.vexora.dto.response.CategoriaResponse;
import com.product.vexora.enums.UnidadeMedida;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoResponseDto(
        UUID id,
        String nome,
        CategoriaResponse categoria,
        UnidadeMedida unidade,
        BigDecimal precoCompra,
        BigDecimal precoVenda,
        BigDecimal estoqueAtual,
        BigDecimal estoqueMinimo
) {}