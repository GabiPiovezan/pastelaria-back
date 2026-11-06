package com.pastelaria.familia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ProdutoDTO(
        String id,

        @NotBlank(message = "O nome do produto é obrigatório")
        String nome,

        String descricao,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser maior do que zero")
        BigDecimal preco,

        @NotBlank(message = "A categoria é obrigatória")
        String categoria,

        Boolean disponivel
) {}