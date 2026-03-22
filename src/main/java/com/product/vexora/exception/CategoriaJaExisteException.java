package com.product.vexora.exception;

public class CategoriaJaExisteException extends RuntimeException {

    public CategoriaJaExisteException(String nome) {
        super("Categoria com o nome '" + nome + "' já existe");
    }

    public CategoriaJaExisteException() {
        super("Categoria já existe");
    }
}
