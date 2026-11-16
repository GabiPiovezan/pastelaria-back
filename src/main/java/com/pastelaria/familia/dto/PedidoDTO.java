package com.pastelaria.familia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private String usuarioId;

    private BigDecimal valorTotal;

    @JsonProperty("endereco")
    @NotBlank(message = "O endereço de entrega é obrigatório")
    private String enderecoEntrega;

    @NotBlank(message = "O tipo de entrega é obrigatório")
    private String tipoEntrega;

    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    @NotBlank(message = "A forma de pagamento é obrigatória")
    private String formaPagamento;

    private List<ItemPedidoDTO> itens;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedidoDTO {
        private String idProduto;
        private Integer quantidade;
    }
}