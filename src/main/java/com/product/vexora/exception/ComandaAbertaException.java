package com.product.vexora.exception;

public class ComandaAbertaException extends RuntimeException {

    public ComandaAbertaException() {
        super("Já existe uma comanda aberta.");
    }

    public ComandaAbertaException(Integer mesa, String identificador) {
        super(String.format(
                "Já existe uma comanda aberta para a mesa %d com o identificador '%s'.",
                mesa,
                identificador
        ));
    }
}

