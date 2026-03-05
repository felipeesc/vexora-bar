package com.product.vexora.dto;

import com.product.vexora.enums.CategoriaProduto;
import com.product.vexora.enums.UnidadeMedida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProdutoRequestDto(
        @NotBlank(message = "O nome do produto é obrigatório")
        String nome,

        @NotNull(message = "A categoria é obrigatória")
        CategoriaProduto categoria,

        @NotNull(message = "A unidade de medida é obrigatória")
        UnidadeMedida unidade,

        @NotNull(message = "O preço de compra é obrigatório")
        @PositiveOrZero(message = "O preço de compra deve ser zero ou positivo")
        BigDecimal precoCompra,

        @NotNull(message = "O preço de venda é obrigatório")
        @PositiveOrZero(message = "O preço de venda deve ser zero ou positivo")
        BigDecimal precoVenda,

        @NotNull(message = "O estoque atual é obrigatório")
        @PositiveOrZero(message = "O estoque atual deve ser zero ou positivo")
        BigDecimal estoqueAtual,

        @NotNull(message = "O estoque mínimo é obrigatório")
        @PositiveOrZero(message = "O estoque mínimo deve ser zero ou positivo")
        BigDecimal estoqueMinimo
) {}
