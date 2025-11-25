package com.product.vexora.exception;

public class ComandaFechadaException extends RuntimeException {
    public ComandaFechadaException() {
        super("A comanda já está fechada.");
    }
}
