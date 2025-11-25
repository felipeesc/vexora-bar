package com.product.vexora.controller;

import com.product.vexora.dto.ComandaItemRequestDTO;
import com.product.vexora.dto.ComandaRequestDTO;
import com.product.vexora.dto.ComandaResponseDTO;
import com.product.vexora.service.ComandaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comandas")
public class ComandaController {

    private final ComandaService service;

    @PostMapping("/abrir")
    public ComandaResponseDTO abrir(@RequestBody ComandaRequestDTO dto) {
        return service.abrirComanda(dto);
    }

    @PostMapping("/item")
    public ComandaResponseDTO adicionarItem(@RequestBody ComandaItemRequestDTO dto) {
        return service.adicionarItem(dto);
    }

    @PostMapping("/{id}/fechar")
    public ComandaResponseDTO fechar(@PathVariable UUID id) {
        return service.fecharComanda(id);
    }
}
