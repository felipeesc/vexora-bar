package com.product.vexora.service;

import com.product.vexora.dto.ProdutoRequestDto;
import com.product.vexora.dto.ProdutoResponseDto;

import java.util.List;
import java.util.UUID;

public interface ProdutoService {

    List<ProdutoResponseDto> listarTodos();

    ProdutoResponseDto criar(ProdutoRequestDto dto);

    ProdutoResponseDto atualizar(UUID id, ProdutoRequestDto dto);

    void deletar(UUID id);

    ProdutoResponseDto buscarPorId(UUID id);
}
