package com.product.vexora.exception;

public class MesaObrigatoriaException extends RuntimeException {
    public MesaObrigatoriaException() {
        super("O numerda da mesa é obrigatoria");
    }
}
