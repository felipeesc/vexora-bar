package com.product.vexora.dto.response;

import com.product.vexora.enums.Role;

import java.util.UUID;

public record CurrentUserResponse(
    UUID id,
    String username,
    Role role,
    boolean enabled
) {}
