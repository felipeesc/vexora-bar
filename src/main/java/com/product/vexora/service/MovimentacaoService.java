package com.product.vexora.service;

import com.product.vexora.dto.MovimentacaoDto;
import com.product.vexora.entity.Movimentacao;

public interface MovimentacaoService {

    Movimentacao realizarMovimentacao(MovimentacaoDto req);

}
