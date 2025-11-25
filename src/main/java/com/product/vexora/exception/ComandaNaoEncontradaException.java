package com.product.vexora.exception;

public class ComandaNaoEncontradaException extends RuntimeException {
    public ComandaNaoEncontradaException() {
        super("Comanda não encontrada.");
    }
}

