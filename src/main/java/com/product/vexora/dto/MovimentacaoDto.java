package com.product.vexora.dto;

import com.product.vexora.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record MovimentacaoDto(
        @NotNull(message = "O ID do produto é obrigatório")
        UUID produtoId,

        @NotNull(message = "O tipo de movimentação é obrigatório")
        TipoMovimentacao tipo,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        BigDecimal quantidade,

        String motivo
) {}
