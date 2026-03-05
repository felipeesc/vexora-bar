package com.product.vexora.service;

import com.product.vexora.dto.MovimentacaoDto;
import com.product.vexora.dto.MovimentacaoResponseDTO;
import com.product.vexora.entity.Produto;

import java.util.UUID;

public interface MovimentacaoService {

    MovimentacaoResponseDTO realizarMovimentacao(MovimentacaoDto req);
    void registrarSaidaPorComanda(Produto produto, int quantidade, UUID comandaId);

    void registrarEntradaPorCancelamento(Produto produto, int quantidade, UUID comandaId);
}
