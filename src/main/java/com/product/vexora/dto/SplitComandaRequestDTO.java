package com.product.vexora.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record SplitComandaRequestDTO(
        @NotEmpty(message = "Selecione pelo menos um item para dividir")
        List<UUID> itemIds,
        String cliente
) {}
