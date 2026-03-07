package com.product.vexora.dto.request;

import com.product.vexora.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
    @NotBlank(message = "Username é obrigatório")
    String username,

    @NotBlank(message = "Password é obrigatório")
    String password,

    @NotNull(message = "Role é obrigatório")
    Role role
) {}
