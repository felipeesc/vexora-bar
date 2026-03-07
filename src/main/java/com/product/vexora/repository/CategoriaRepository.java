package com.product.vexora.repository;

import com.product.vexora.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    List<Categoria> findByAtivaTrue();

    Optional<Categoria> findByNome(String nome);

    boolean existsByNome(String nome);
}
