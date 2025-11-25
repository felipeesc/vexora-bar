package com.product.vexora.dto;

import com.product.vexora.enums.CategoriaProduto;
import com.product.vexora.enums.UnidadeMedida;

import java.math.BigDecimal;
import java.util.UUID;

public record ComandaItemDTO(
        UUID id,
        String nome,
        CategoriaProduto categoria,
        UnidadeMedida unidade,
        int quantidade,
        BigDecimal precoUnitario,
        BigDecimal totalItem
) {}
