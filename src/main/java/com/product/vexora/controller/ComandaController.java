package com.product.vexora.controller;

import com.product.vexora.dto.*;
import com.product.vexora.service.ComandaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comandas")
public class ComandaController {

    private final ComandaService service;

    @PostMapping("/abrir")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ComandaResponseDTO> abrir(@Valid @RequestBody ComandaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.abrirComanda(dto));
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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        LocalDateTime inicioDateTime = inicio != null ? inicio.atStartOfDay() : null;
        LocalDateTime fimDateTime = fim != null ? fim.atTime(LocalTime.MAX) : null;
        return service.listar(aberta, mesa, inicioDateTime, fimDateTime);
    }

    @PostMapping("/item")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ComandaResponseDTO> adicionarItem(@Valid @RequestBody ComandaItemRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.adicionarItem(dto));
    }

    @PostMapping("/{id}/fechar")
    @PreAuthorize("isAuthenticated()")
    public ComandaResponseDTO fechar(@PathVariable UUID id, @Valid @RequestBody FechamentoRequestDTO dto) {
        return service.fecharComanda(id, dto);
    }

    @PostMapping("/mesa/{mesa}/fechar")
    @PreAuthorize("isAuthenticated()")
    public List<ComandaResponseDTO> fecharMesa(@PathVariable Integer mesa, @Valid @RequestBody FechamentoMesaRequestDTO dto) {
        return service.fecharMesa(mesa, dto);
    }

    @PostMapping("/item/{itemId}/cancelar")
    @PreAuthorize("isAuthenticated()")
    public ComandaResponseDTO cancelarItem(@PathVariable UUID itemId, @Valid @RequestBody CancelItemRequestDTO dto) {
        return service.cancelarItem(itemId, dto);
    }

    @PostMapping("/{id}/dividir")
    @PreAuthorize("hasRole('ADMIN')")
    public ComandaResponseDTO dividirComanda(@PathVariable UUID id, @Valid @RequestBody SplitComandaRequestDTO dto) {
        return service.dividirComanda(id, dto);
    }

    @PostMapping("/{id}/transferir")
    @PreAuthorize("isAuthenticated()")
    public ComandaResponseDTO transferirItens(@PathVariable UUID id, @Valid @RequestBody TransferItemRequestDTO dto) {
        return service.transferirItens(id, dto);
    }

    @PostMapping("/juntar")
    @PreAuthorize("isAuthenticated()")
    public ComandaResponseDTO juntarComandas(@Valid @RequestBody MergeComandaRequestDTO dto) {
        return service.juntarComandas(dto);
    }

    @PostMapping("/{id}/reabrir")
    @PreAuthorize("hasRole('ADMIN')")
    public ComandaResponseDTO reabrirComanda(@PathVariable UUID id) {
        return service.reabrirComanda(id);
    }

    @GetMapping("/{id}/calcular")
    @PreAuthorize("isAuthenticated()")
    public ComandaResponseDTO calcular(@PathVariable UUID id) {
        return service.calcular(id);
    }

    @DeleteMapping("/item/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ComandaResponseDTO removerItem(@PathVariable UUID itemId) {
        return service.cancelarItem(itemId, new CancelItemRequestDTO("Removido pelo operador"));
    }
}
