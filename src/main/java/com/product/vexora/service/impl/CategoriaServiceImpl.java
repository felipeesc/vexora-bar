package com.product.vexora.service.impl;

import com.product.vexora.dto.request.CreateCategoriaRequest;
import com.product.vexora.dto.response.CategoriaResponse;
import com.product.vexora.entity.Categoria;
import com.product.vexora.entity.User;
import com.product.vexora.enums.Role;
import com.product.vexora.exception.UnauthorizedRoleException;
import com.product.vexora.repository.CategoriaRepository;
import com.product.vexora.service.CategoriaService;
import com.product.vexora.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UserService userService;

    @Override
    public List<CategoriaResponse> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::toCategoriaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoriaResponse> listarCategoriasAtivas() {
        return categoriaRepository.findByAtivaTrue().stream()
                .map(this::toCategoriaResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoriaResponse criarCategoria(CreateCategoriaRequest request, String currentUserUsername) {
        User currentUser = userService.loadByUsername(currentUserUsername);

        // Validar permissões - apenas ADMIN e GERENTE podem criar categorias
        if (currentUser.getRole() == Role.FUNCIONARIO) {
            throw new UnauthorizedRoleException("Funcionário não tem permissão para criar categorias");
        }

        // Verificar se a categoria já existe
        if (categoriaRepository.existsByNome(request.nome())) {
            throw new RuntimeException("Categoria com esse nome já existe");
        }

        Categoria categoria = new Categoria();
        categoria.setNome(request.nome());
        categoria.setDescricao(request.descricao());
        categoria.setAtiva(true);

        Categoria savedCategoria = categoriaRepository.save(categoria);
        return toCategoriaResponse(savedCategoria);
    }

    @Override
    public CategoriaResponse obterCategoriaPorId(UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        return toCategoriaResponse(categoria);
    }

    @Override
    @Transactional
    public CategoriaResponse atualizarCategoria(UUID id, CreateCategoriaRequest request, String currentUserUsername) {
        User currentUser = userService.loadByUsername(currentUserUsername);

        // Validar permissões - apenas ADMIN e GERENTE podem atualizar categorias
        if (currentUser.getRole() == Role.FUNCIONARIO) {
            throw new UnauthorizedRoleException("Funcionário não tem permissão para atualizar categorias");
        }

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // Verificar se o novo nome já existe em outra categoria
        if (!categoria.getNome().equals(request.nome()) &&
            categoriaRepository.existsByNome(request.nome())) {
            throw new RuntimeException("Categoria com esse nome já existe");
        }

        categoria.setNome(request.nome());
        categoria.setDescricao(request.descricao());

        Categoria updatedCategoria = categoriaRepository.save(categoria);
        return toCategoriaResponse(updatedCategoria);
    }

    @Override
    @Transactional
    public void deletarCategoria(UUID id, String currentUserUsername) {
        User currentUser = userService.loadByUsername(currentUserUsername);

        // Validar permissões - apenas ADMIN pode deletar categorias
        if (currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedRoleException("Apenas administradores podem deletar categorias");
        }

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // Soft delete - marcar como inativa ao invés de deletar
        categoria.setAtiva(false);
        categoriaRepository.save(categoria);
    }

    private CategoriaResponse toCategoriaResponse(Categoria categoria) {
        return new CategoriaResponse(
            categoria.getId(),
            categoria.getNome(),
            categoria.getDescricao(),
            categoria.isAtiva(),
            categoria.getCriadoEm()
        );
    }
}
