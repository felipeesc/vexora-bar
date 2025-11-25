package com.product.vexora.service;

import com.product.vexora.dto.ComandaItemRequestDTO;
import com.product.vexora.dto.ComandaRequestDTO;
import com.product.vexora.dto.ComandaResponseDTO;

import java.util.UUID;

public interface ComandaService {

    ComandaResponseDTO abrirComanda(ComandaRequestDTO dto);

    ComandaResponseDTO adicionarItem(ComandaItemRequestDTO dto);

    ComandaResponseDTO fecharComanda(UUID id);
}

