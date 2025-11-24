package com.product.vexora.controller;

import com.product.vexora.dto.MovimentacaoDto;
import com.product.vexora.entity.Movimentacao;
import com.product.vexora.service.MovimentacaoService;
import lombok.RequiredArgsConstructor;
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
    public Movimentacao movimentar(@RequestBody MovimentacaoDto req) {
        return service.realizarMovimentacao(req);
    }
}
