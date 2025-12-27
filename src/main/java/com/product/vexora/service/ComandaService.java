package com.product.vexora.service;

import com.product.vexora.dto.ComandaItemRequestDTO;
import com.product.vexora.dto.ComandaRequestDTO;
import com.product.vexora.dto.ComandaResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ComandaService {

    ComandaResponseDTO abrirComanda(ComandaRequestDTO dto);

    ComandaResponseDTO adicionarItem(ComandaItemRequestDTO dto);

    ComandaResponseDTO removerItem(UUID itemId);

    ComandaResponseDTO fecharComanda(UUID id);

    ComandaResponseDTO calcular(UUID id);

    ComandaResponseDTO buscarPorId(UUID id);

    List<ComandaResponseDTO> listar(
            Boolean aberta,
            Integer mesa,
            LocalDateTime inicio,
            LocalDateTime fim
    );
}


