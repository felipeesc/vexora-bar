package com.product.vexora.repository;

import com.product.vexora.entity.Comanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, UUID> {

    List<Comanda> findByAbertaTrue();

    long countByMesaAndAbertaTrue(Integer mesa);

    @Query("SELECT c FROM Comanda c " +
            "WHERE (:aberta IS NULL OR c.aberta = :aberta) " +
            "AND (:mesa IS NULL OR c.mesa = :mesa) " +
            "AND (CAST(:inicio AS timestamp) IS NULL OR c.abertura >= :inicio) " +
            "AND (CAST(:fim AS timestamp) IS NULL OR c.abertura <= :fim) " +
            "ORDER BY c.abertura DESC")
    List<Comanda> filtrar(
            @Param("aberta") Boolean aberta,
            @Param("mesa") Integer mesa,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}
