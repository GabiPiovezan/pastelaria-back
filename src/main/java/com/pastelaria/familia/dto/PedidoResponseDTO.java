package com.pastelaria.familia.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoResponseDTO(
        String id,
        String usuarioId,
        String status,
        BigDecimal valorTotal,
        String enderecoEntrega,
        LocalDateTime criadoEm
) {}