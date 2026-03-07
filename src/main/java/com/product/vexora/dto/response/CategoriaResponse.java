package com.product.vexora.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoriaResponse(
    UUID id,
    String nome,
    String descricao,
    boolean ativa,
    LocalDateTime criadoEm
) {}
