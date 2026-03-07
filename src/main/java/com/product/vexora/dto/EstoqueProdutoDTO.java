package com.product.vexora.dto;

import com.product.vexora.dto.response.CategoriaResponse;
import com.product.vexora.enums.UnidadeMedida;

import java.math.BigDecimal;
import java.util.UUID;

public record EstoqueProdutoDTO(
        UUID id,
        String nome,
        CategoriaResponse categoria,
        UnidadeMedida unidade,
        BigDecimal estoqueAtual,
        BigDecimal estoqueMinimo,
        boolean abaixoDoMinimo
) {}
