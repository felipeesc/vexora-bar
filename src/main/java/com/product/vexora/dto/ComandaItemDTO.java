package com.product.vexora.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.product.vexora.enums.CategoriaProduto;
import com.product.vexora.enums.UnidadeMedida;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ComandaItemDTO(
        UUID id,
        String nome,
        CategoriaProduto categoria,
        UnidadeMedida unidade,
        int quantidade,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime dataHora,

        BigDecimal precoUnitario,
        BigDecimal totalItem
) {}
