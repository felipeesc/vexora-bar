package com.product.vexora.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record TransferItemRequestDTO(
        @NotEmpty(message = "Selecione pelo menos um item para transferir")
        List<UUID> itemIds,
        UUID comandaDestinoId
) {}
