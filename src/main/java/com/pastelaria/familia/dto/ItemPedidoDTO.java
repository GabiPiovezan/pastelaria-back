package com.pastelaria.familia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ItemPedidoDTO (
    @NotBlank(message = "O ID do pedido é obrigatório")
    String pedidoId,

    @NotBlank(message = "O ID do produto é obrigatório")
    String produtoId,

    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    Integer quantidade,

    @NotNull(message = "O preço unitário é obrigatório")
    @Positive(message = "O preço unitário deve ser positivo")
    BigDecimal precoUnitario
) {}

