package com.product.vexora.entity;

import com.product.vexora.enums.MetodoPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comanda_id", nullable = false)
    private Comanda comanda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPagamento metodo;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    public Pagamento(Comanda comanda, MetodoPagamento metodo, BigDecimal valor) {
        this.comanda = comanda;
        this.metodo = metodo;
        this.valor = valor;
    }
}
