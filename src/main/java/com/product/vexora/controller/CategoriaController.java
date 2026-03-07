package com.product.vexora.controller;

import com.product.vexora.dto.request.CreateCategoriaRequest;
import com.product.vexora.dto.response.CategoriaResponse;
import com.product.vexora.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        List<CategoriaResponse> categorias = categoriaService.listarCategoriasAtivas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/todas")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<CategoriaResponse>> listarTodasCategorias() {
        List<CategoriaResponse> categorias = categoriaService.listarCategorias();
        return ResponseEntity.ok(categorias);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<CategoriaResponse> criarCategoria(
            @Valid @RequestBody CreateCategoriaRequest request,
            Authentication authentication) {
        CategoriaResponse categoria = categoriaService.criarCategoria(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obterCategoriaPorId(@PathVariable UUID id) {
        CategoriaResponse categoria = categoriaService.obterCategoriaPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<CategoriaResponse> atualizarCategoria(
            @PathVariable UUID id,
            @Valid @RequestBody CreateCategoriaRequest request,
            Authentication authentication) {
        CategoriaResponse categoria = categoriaService.atualizarCategoria(id, request, authentication.getName());
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarCategoria(@PathVariable UUID id, Authentication authentication) {
        categoriaService.deletarCategoria(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
