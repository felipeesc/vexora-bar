package com.product.vexora.service;

import com.product.vexora.dto.ProdutoRequestDto;
import com.product.vexora.dto.ProdutoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProdutoService {

    Page<ProdutoResponseDto> listarTodos(Pageable pageable);

    ProdutoResponseDto criar(ProdutoRequestDto dto);

    ProdutoResponseDto atualizar(UUID id, ProdutoRequestDto dto);

    void deletar(UUID id);

    ProdutoResponseDto buscarPorId(UUID id);
}
