package com.product.vexora.dto;

import java.util.UUID;

public record ComandaItemRequestDTO(
        UUID comandaId,
        UUID produtoId,
        int quantidade
) {}


