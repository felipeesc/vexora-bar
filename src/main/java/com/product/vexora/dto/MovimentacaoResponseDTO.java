package com.product.vexora.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.product.vexora.enums.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentacaoResponseDTO(
        Long id,
        String produtoNome,
        TipoMovimentacao tipo,
        BigDecimal quantidade,
        String motivo,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime dataHora
) {}
