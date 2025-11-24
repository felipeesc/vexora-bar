package com.product.vexora.controller;

import com.product.vexora.dto.ProdutoRequestDto;
import com.product.vexora.dto.ProdutoResponseDto;
import com.product.vexora.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    public List<ProdutoResponseDto> list() {
        return produtoService.listarTodos();
    }

    @PostMapping
    public ProdutoResponseDto create(@RequestBody ProdutoRequestDto dto) {
        return produtoService.criar(dto);
    }

    @PutMapping("/{id}")
    public ProdutoResponseDto update(@PathVariable UUID id, @RequestBody ProdutoRequestDto dto) {
        return produtoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        produtoService.deletar(id);
    }

    @GetMapping("/{id}")
    public ProdutoResponseDto getById(@PathVariable UUID id) {
        return produtoService.buscarPorId(id);
    }
}
