package com.product.vexora.entity;

import com.product.vexora.enums.CategoriaProduto;
import com.product.vexora.enums.UnidadeMedida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private CategoriaProduto categoria;

    @Enumerated(EnumType.STRING)
    private UnidadeMedida unidade;
    private BigDecimal precoCompra;
    private BigDecimal precoVenda;
    private BigDecimal estoqueAtual;
    private BigDecimal estoqueMinimo;
}