package com.product.vexora.controller;

import com.product.vexora.dto.FaturamentoDTO;
import com.product.vexora.dto.MovimentacaoResponseDTO;
import com.product.vexora.dto.ProdutoMaisVendidoDto;
import com.product.vexora.dto.RelatorioEstoqueDTO;
import com.product.vexora.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/faturamento/diario")
    public FaturamentoDTO faturamentoDiario(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        return relatorioService.faturamentoDiario(data != null ? data : LocalDate.now());
    }

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
    @GetMapping("/produtos/mais-vendidos/dia")
    public List<ProdutoMaisVendidoDto> produtosMaisVendidosDia(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        return relatorioService.produtosMaisVendidosDia(data != null ? data : LocalDate.now());
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/estoque")
    public RelatorioEstoqueDTO relatorioEstoque() {
        return relatorioService.relatorioEstoque();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/estoque/movimentacoes")
    public List<MovimentacaoResponseDTO> historicoMovimentacoes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        return relatorioService.historicoMovimentacoes(inicio, fim);
    }
}

