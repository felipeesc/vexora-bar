package com.product.vexora.service;

import com.product.vexora.dto.MovimentacaoDto;
import com.product.vexora.entity.Movimentacao;
import com.product.vexora.entity.Produto;

import java.util.UUID;

public interface MovimentacaoService {

    Movimentacao realizarMovimentacao(MovimentacaoDto req);
    void registrarSaidaPorComanda(Produto produto, int quantidade, UUID comandaId);

    void registrarEntradaPorCancelamento(Produto produto, int quantidade, UUID comandaId);
}
