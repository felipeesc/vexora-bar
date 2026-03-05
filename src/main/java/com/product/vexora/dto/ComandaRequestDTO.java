package com.product.vexora.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ComandaRequestDTO(
        @NotNull(message = "O número da mesa é obrigatório")
        @Positive(message = "O número da mesa deve ser positivo")
        Integer mesa,

        String identificador,
        String cliente
) {}

