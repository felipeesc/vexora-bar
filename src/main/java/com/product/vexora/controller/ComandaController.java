package com.product.vexora.controller;

import com.product.vexora.dto.ComandaItemRequestDTO;
import com.product.vexora.dto.ComandaRequestDTO;
import com.product.vexora.dto.ComandaResponseDTO;
import com.product.vexora.service.ComandaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comandas")
public class ComandaController {

    private final ComandaService service;

    @PostMapping("/abrir")
    @PreAuthorize("isAuthenticated()")
    public ComandaResponseDTO abrir(@RequestBody ComandaRequestDTO dto) {
        return service.abrirComanda(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ComandaResponseDTO buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<ComandaResponseDTO> listar(
            @RequestParam(required = false) Boolean aberta,
            @RequestParam(required = false) Integer mesa,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        return service.listar(aberta, mesa, inicio, fim);
    }


    @PostMapping("/item")
    @PreAuthorize("isAuthenticated()")
    public ComandaResponseDTO adicionarItem(@RequestBody ComandaItemRequestDTO dto) {
        return service.adicionarItem(dto);
    }

    @PostMapping("/{id}/fechar")
    @PreAuthorize("isAuthenticated()")
    public ComandaResponseDTO fechar(@PathVariable UUID id) {
        return service.fecharComanda(id);
    }

    @GetMapping("/{id}/calcular")
    @PreAuthorize("isAuthenticated()")
    public ComandaResponseDTO calcular(@PathVariable UUID id) {
        return service.calcular(id);
    }


    @DeleteMapping("/item/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ComandaResponseDTO removerItem(@PathVariable UUID itemId) {
        return service.removerItem(itemId);
    }

}
