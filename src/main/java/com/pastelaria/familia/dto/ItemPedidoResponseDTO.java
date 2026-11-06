package com.pastelaria.familia.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        String id,
        String pedidoId,
        String produtoId,
        Integer quantidade,
        BigDecimal precoUnitario
) {}