package com.product.vexora.service;

import com.product.vexora.dto.ComandaItemRequestDTO;
import com.product.vexora.dto.ComandaRequestDTO;
import com.product.vexora.dto.ComandaResponseDTO;

import java.util.UUID;

public interface ComandaService {

    ComandaResponseDTO abrirComanda(ComandaRequestDTO dto);

    ComandaResponseDTO adicionarItem(ComandaItemRequestDTO dto);

    ComandaResponseDTO removerItem(UUID itemId);

    ComandaResponseDTO fecharComanda(UUID id);

    ComandaResponseDTO calcular(UUID comandaId);

}

