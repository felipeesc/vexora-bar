package com.product.vexora.dto;

import com.product.vexora.enums.TipoMovimentacao;

import java.math.BigDecimal;
import java.util.UUID;

public record MovimentacaoDto(UUID produtoId,
                              TipoMovimentacao tipo,
                              BigDecimal quantidade,
                              String motivo
) {}
