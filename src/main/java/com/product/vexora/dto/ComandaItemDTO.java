package com.product.vexora.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.product.vexora.dto.response.CategoriaResponse;
import com.product.vexora.enums.UnidadeMedida;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ComandaItemDTO(
        UUID id,
        String nome,
        CategoriaResponse categoria,
        UnidadeMedida unidade,
        int quantidade,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dataHora,

        BigDecimal precoUnitario,
        BigDecimal totalItem,
        boolean cancelado,
        String motivoCancelamento
) {}
