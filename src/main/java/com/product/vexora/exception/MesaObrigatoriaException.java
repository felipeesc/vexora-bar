package com.product.vexora.exception;

public class MesaObrigatoriaException extends RuntimeException {
    public MesaObrigatoriaException() {
        super("O número da mesa é obrigatório.");
    }
}
