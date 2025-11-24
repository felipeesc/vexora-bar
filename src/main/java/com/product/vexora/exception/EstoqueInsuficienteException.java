package com.product.vexora.exception;

public class EstoqueInsuficienteException extends RuntimeException {
    public EstoqueInsuficienteException(String produtoNome) {
        super("Estoque insuficiente para o produto: " + produtoNome);
    }
}
