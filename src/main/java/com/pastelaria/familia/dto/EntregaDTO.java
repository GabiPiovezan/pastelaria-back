package com.pastelaria.familia.dto;

import jakarta.validation.constraints.NotBlank;

public record EntregaDTO (
        @NotBlank(message = "O ID do pedido é obrigatório")
        String pedidoId,

        String statusEntrega
) {}

