package com.product.vexora.dto;

import com.product.vexora.enums.MetodoPagamento;

import java.math.BigDecimal;

public record PagamentoDTO(
        MetodoPagamento metodo,
        BigDecimal valor
) {}
