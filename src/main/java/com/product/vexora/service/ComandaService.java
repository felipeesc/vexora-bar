package com.product.vexora.service;


import com.product.vexora.dto.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ComandaService {

    ComandaResponseDTO abrirComanda(ComandaRequestDTO dto);

    ComandaResponseDTO adicionarItem(ComandaItemRequestDTO dto);

    ComandaResponseDTO cancelarItem(UUID itemId, CancelItemRequestDTO dto);

    ComandaResponseDTO fecharComanda(UUID id, FechamentoRequestDTO dto);

    List<ComandaResponseDTO> fecharMesa(Integer mesa, FechamentoMesaRequestDTO dto);

    ComandaResponseDTO dividirComanda(UUID comandaId, SplitComandaRequestDTO dto);

    ComandaResponseDTO transferirItens(UUID comandaOrigemId, TransferItemRequestDTO dto);

    ComandaResponseDTO juntarComandas(MergeComandaRequestDTO dto);

    ComandaResponseDTO reabrirComanda(UUID id);

    ComandaResponseDTO calcular(UUID id);

    ComandaResponseDTO buscarPorId(UUID id);

    List<ComandaResponseDTO> listar(
            Boolean aberta,
            Integer mesa,
            LocalDateTime inicio,
            LocalDateTime fim
    );
}


