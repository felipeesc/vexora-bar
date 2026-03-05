package com.product.vexora.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ComandaResponseDTO(
        UUID id,
        Integer mesa,
        String cliente,
        boolean aberta,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime abertura,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fechamento,

        List<ComandaItemDTO> itens,
        BigDecimal total
) {}


