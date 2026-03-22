package com.product.vexora.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Unicidade controlada por partial unique index no PostgreSQL (ver migration.sql):
 *
 *   CREATE UNIQUE INDEX uq_comanda_mesa_identificador_aberta
 *     ON tb_comanda (mesa, identificador)
 *     WHERE aberta = true;
 *
 * Garante: somente uma comanda ABERTA por mesa+identificador.
 * Permite: múltiplas comandas FECHADAS com o mesmo identificador.
 */
@Getter
@Setter
@Entity
@Table(name = "tb_comanda")
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private Integer mesa;

    private String cliente;

    @Column(nullable = false)
    private String identificador;

    @Column(nullable = false)
    private boolean aberta;

    @Column(nullable = false)
    private LocalDateTime abertura;

    private LocalDateTime fechamento;

    @OneToMany(
            mappedBy = "comanda",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ComandaItem> itens = new ArrayList<>();

    @OneToMany(
            mappedBy = "comanda",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Pagamento> pagamentos = new ArrayList<>();
}
