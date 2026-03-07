package com.product.vexora.service;

import com.product.vexora.dto.request.CreateCategoriaRequest;
import com.product.vexora.dto.response.CategoriaResponse;

import java.util.List;
import java.util.UUID;

public interface CategoriaService {

    List<CategoriaResponse> listarCategorias();

    List<CategoriaResponse> listarCategoriasAtivas();

    CategoriaResponse criarCategoria(CreateCategoriaRequest request, String currentUserUsername);

    CategoriaResponse obterCategoriaPorId(UUID id);

    CategoriaResponse atualizarCategoria(UUID id, CreateCategoriaRequest request, String currentUserUsername);

    void deletarCategoria(UUID id, String currentUserUsername);
}
