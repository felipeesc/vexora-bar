package com.product.vexora.exception;

import java.util.UUID;

public class CategoriaNaoEncontradaException extends RuntimeException {

    public CategoriaNaoEncontradaException(UUID id) {
        super("Categoria não encontrada com ID: " + id);
    }

    public CategoriaNaoEncontradaException(String nome) {
        super("Categoria não encontrada: " + nome);
    }

    public CategoriaNaoEncontradaException() {
        super("Categoria não encontrada");
    }
}
