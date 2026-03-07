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

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime abertura,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime fechamento,

        List<ComandaItemDTO> itens,
        BigDecimal total,
        List<PagamentoResponseDTO> pagamentos
) {}


