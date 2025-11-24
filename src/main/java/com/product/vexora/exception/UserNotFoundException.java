package com.product.vexora.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("Usuário não encontrado: " + username);
    }
}
