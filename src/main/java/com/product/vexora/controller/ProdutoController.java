package com.product.vexora.controller;

import com.product.vexora.dto.ProdutoRequestDto;
import com.product.vexora.dto.ProdutoResponseDto;
import com.product.vexora.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<ProdutoResponseDto> list() {
        return produtoService.listarTodos();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ProdutoResponseDto> create(@Valid @RequestBody ProdutoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.criar(dto));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ProdutoResponseDto update(@PathVariable UUID id, @Valid @RequestBody ProdutoRequestDto dto) {
        return produtoService.atualizar(id, dto);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        produtoService.deletar(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ProdutoResponseDto getById(@PathVariable UUID id) {
        return produtoService.buscarPorId(id);
    }
}
