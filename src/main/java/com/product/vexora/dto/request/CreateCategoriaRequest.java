package com.product.vexora.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoriaRequest(
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    String nome,

    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    String descricao
) {}
