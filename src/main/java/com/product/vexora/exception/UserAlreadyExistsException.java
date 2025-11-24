package com.product.vexora.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("Username já existe: " + username);
    }
}
