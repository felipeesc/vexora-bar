package com.product.vexora.dto;

import com.product.vexora.enums.MetodoPagamento;

import java.math.BigDecimal;

public record PagamentoResponseDTO(
        Long id,
        MetodoPagamento metodo,
        BigDecimal valor
) {}
