package com.pastelaria.familia.dto;

import java.time.LocalDateTime;

public record EntregaResponseDTO(
        String id,
        String pedidoId,
        String statusEntrega,
        LocalDateTime atualizadoEm
) {}