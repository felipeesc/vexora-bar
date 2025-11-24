package com.product.vexora.dto;

import com.product.vexora.enums.Role;

public record SignupDto(
        String username,
        String password,
        Role role
) {}