package com.product.vexora.dto;

import java.util.List;

public record RelatorioEstoqueDTO(
        long totalProdutos,
        long produtosAbaixoDoMinimo,
        List<EstoqueProdutoDTO> produtos
) {}
