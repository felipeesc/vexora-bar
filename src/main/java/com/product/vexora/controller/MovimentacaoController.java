package com.product.vexora.controller;

import com.product.vexora.dto.MovimentacaoDto;
import com.product.vexora.dto.MovimentacaoResponseDTO;
import com.product.vexora.service.MovimentacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoController {

    private final MovimentacaoService service;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<MovimentacaoResponseDTO> movimentar(@Valid @RequestBody MovimentacaoDto req) {
        MovimentacaoResponseDTO response = service.realizarMovimentacao(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
