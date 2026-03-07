package com.product.vexora.dto;

import jakarta.validation.constraints.NotBlank;

public record CancelItemRequestDTO(
        @NotBlank(message = "O motivo do cancelamento é obrigatório")
        String motivo
) {}
