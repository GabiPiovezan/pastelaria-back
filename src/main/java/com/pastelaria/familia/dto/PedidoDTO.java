package com.pastelaria.familia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record PedidoDTO(
        @NotBlank(message = "O ID do usuário é obrigatório")
        String usuarioId,

        @NotNull(message = "O valor total é obrigatório")
        @Positive(message = "O valor total deve ser positivo")
        BigDecimal valorTotal,

        @NotBlank(message = "O endereço de entrega é obrigatório")
        String enderecoEntrega
) {}