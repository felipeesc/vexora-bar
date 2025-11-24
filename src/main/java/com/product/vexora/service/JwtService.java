package com.product.vexora.service;

import com.product.vexora.entity.User;

public interface JwtService {

    String generateToken(User user);

    String extractUsername(String token);

    boolean validateToken(String token, User user);
}
