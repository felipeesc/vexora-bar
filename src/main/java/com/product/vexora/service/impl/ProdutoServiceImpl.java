package com.product.vexora.service.impl;

import com.product.vexora.dto.ProdutoRequestDto;
import com.product.vexora.dto.ProdutoResponseDto;
import com.product.vexora.dto.response.CategoriaResponse;
import com.product.vexora.entity.Categoria;
import com.product.vexora.entity.Produto;
import com.product.vexora.exception.ProdutoNotFoundException;
import com.product.vexora.repository.CategoriaRepository;
import com.product.vexora.repository.ProdutoRepository;
import com.product.vexora.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public List<ProdutoResponseDto> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProdutoResponseDto criar(ProdutoRequestDto dto) {
        Produto produto = fromRequestDto(dto);
        Produto salvo = produtoRepository.save(produto);
        return toResponseDto(salvo);
    }

    @Override
    public ProdutoResponseDto atualizar(UUID id, ProdutoRequestDto dto) {
        Produto existente = buscarPorIdEntity(id);
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada: " + dto.categoriaId()));

        existente.setNome(dto.nome());
        existente.setCategoria(categoria);
        existente.setUnidade(dto.unidade());
        existente.setPrecoCompra(dto.precoCompra());
        existente.setPrecoVenda(dto.precoVenda());
        existente.setEstoqueAtual(dto.estoqueAtual());
        existente.setEstoqueMinimo(dto.estoqueMinimo());
        Produto atualizado = produtoRepository.save(existente);
        return toResponseDto(atualizado);
    }

    @Override
    public void deletar(UUID id) {
        Produto existente = buscarPorIdEntity(id);
        produtoRepository.delete(existente);
    }

    @Override
    public ProdutoResponseDto buscarPorId(UUID id) {
        Produto produto = buscarPorIdEntity(id);
        return toResponseDto(produto);
    }

    private Produto fromRequestDto(ProdutoRequestDto dto) {
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada: " + dto.categoriaId()));

        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setCategoria(categoria);
        produto.setUnidade(dto.unidade());
        produto.setPrecoCompra(dto.precoCompra());
        produto.setPrecoVenda(dto.precoVenda());
        produto.setEstoqueAtual(dto.estoqueAtual());
        produto.setEstoqueMinimo(dto.estoqueMinimo());
        return produto;
    }

    private ProdutoResponseDto toResponseDto(Produto produto) {
        CategoriaResponse categoriaResponse = new CategoriaResponse(
                produto.getCategoria().getId(),
                produto.getCategoria().getNome(),
                produto.getCategoria().getDescricao(),
                produto.getCategoria().isAtiva(),
                produto.getCategoria().getCriadoEm()
        );

        return new ProdutoResponseDto(
                produto.getId(),
                produto.getNome(),
                categoriaResponse,
                produto.getUnidade(),
                produto.getPrecoCompra(),
                produto.getPrecoVenda(),
                produto.getEstoqueAtual(),
                produto.getEstoqueMinimo()
        );
    }

    private Produto buscarPorIdEntity(UUID id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException(id));
    }
}
