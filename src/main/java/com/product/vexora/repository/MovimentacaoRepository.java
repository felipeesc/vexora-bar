package com.product.vexora.repository;

import com.product.vexora.dto.FaturamentoDTO;
import com.product.vexora.dto.ProdutoMaisVendidoDto;
import com.product.vexora.entity.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, UUID> {


    @Query("SELECT new com.product.vexora.dto.FaturamentoDTO(" +
            "SUM(m.quantidade * m.produto.precoVenda), " +
            "SUM(m.quantidade * (m.produto.precoVenda - m.produto.precoCompra))) " +
            "FROM Movimentacao m " +
            "WHERE m.dataHora BETWEEN :inicio AND :fim")
    FaturamentoDTO faturamentoPeriodo(@Param("inicio") LocalDateTime inicio,
                                      @Param("fim") LocalDateTime fim);



    @Query("SELECT new com.product.vexora.dto.ProdutoMaisVendidoDto(" +
            "m.produto.nome, SUM(m.quantidade), m.produto.estoqueAtual) " +
            "FROM Movimentacao m " +
            "WHERE m.tipo = 'saida' AND m.dataHora BETWEEN :inicio AND :fim " +
            "GROUP BY m.produto.nome, m.produto.estoqueAtual " +
            "ORDER BY SUM(m.quantidade) DESC")
    List<ProdutoMaisVendidoDto> produtosMaisVendidos(@Param("inicio") LocalDateTime inicio,
                                                     @Param("fim") LocalDateTime fim);

}

