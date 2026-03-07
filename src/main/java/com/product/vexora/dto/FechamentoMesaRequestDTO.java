package com.product.vexora.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record FechamentoMesaRequestDTO(
        @NotEmpty(message = "É necessário informar pelo menos um pagamento")
        List<PagamentoDTO> pagamentos
) {}
