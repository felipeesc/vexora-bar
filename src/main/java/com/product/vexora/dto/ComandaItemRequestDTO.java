package com.product.vexora.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ComandaItemRequestDTO(
        @NotNull(message = "O ID da comanda é obrigatório")
        UUID comandaId,

        @NotNull(message = "O ID do produto é obrigatório")
        UUID produtoId,

        @Positive(message = "A quantidade deve ser maior que zero")
        int quantidade
) {}


