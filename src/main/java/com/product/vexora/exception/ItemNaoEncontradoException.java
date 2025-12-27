package com.product.vexora.exception;

import java.util.UUID;

public class ItemNaoEncontradoException extends RuntimeException {
    public ItemNaoEncontradoException(UUID itemId) {
        super("Item não encontradO.");
    }
}

