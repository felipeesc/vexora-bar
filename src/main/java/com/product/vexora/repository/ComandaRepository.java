package com.product.vexora.repository;

import com.product.vexora.entity.Comanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, UUID> {

    List<Comanda> findByAbertaTrue();

    long countByMesaAndAbertaTrue(Integer mesa);

}
