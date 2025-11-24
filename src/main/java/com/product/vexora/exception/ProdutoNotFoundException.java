package com.product.vexora.exception;

import java.util.UUID;

public class ProdutoNotFoundException extends RuntimeException {
    public ProdutoNotFoundException(UUID produtoId) {
        super("Produto não encontrado com id: " + produtoId);
    }
}
