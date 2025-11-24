package com.product.vexora.controller;

import com.product.vexora.dto.FaturamentoDTO;
import com.product.vexora.dto.ProdutoMaisVendidoDto;
import com.product.vexora.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/faturamento/semanal")
    public FaturamentoDTO faturamentoSemanal() {
        return relatorioService.faturamentoSemanal();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/faturamento/mensal")
    public FaturamentoDTO faturamentoMensal() {
        return relatorioService.faturamentoMensal();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/produtos/mais-vendidos/semana")
    public List<ProdutoMaisVendidoDto> produtosMaisVendidosSemana() {
        return relatorioService.produtosMaisVendidosSemana();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/produtos/mais-vendidos/mes")
    public List<ProdutoMaisVendidoDto> produtosMaisVendidosMes() {
        return relatorioService.produtosMaisVendidosMes();
    }
}

