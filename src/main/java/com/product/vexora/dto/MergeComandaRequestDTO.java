package com.product.vexora.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record MergeComandaRequestDTO(
        @NotEmpty(message = "Selecione pelo menos duas comandas para juntar")
        @Size(min = 2, message = "É necessário pelo menos duas comandas para juntar")
        List<UUID> comandaIds
) {}
