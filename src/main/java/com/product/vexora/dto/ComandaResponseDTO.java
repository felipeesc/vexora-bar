package com.product.vexora.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ComandaResponseDTO(
        UUID id,
        Integer mesa,
        String cliente,
        boolean aberta,
        List<ComandaItemDTO> itens,
        BigDecimal total
) {}


