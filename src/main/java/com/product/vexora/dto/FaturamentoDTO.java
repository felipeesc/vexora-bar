package com.product.vexora.dto;

import java.math.BigDecimal;

public record FaturamentoDTO(
        BigDecimal faturamentoBruto,
        BigDecimal faturamentoLiquido
) {
}
