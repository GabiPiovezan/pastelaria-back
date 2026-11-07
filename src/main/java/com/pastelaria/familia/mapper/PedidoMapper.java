package com.pastelaria.familia.mapper;

import com.pastelaria.familia.dto.PedidoDTO;
import com.pastelaria.familia.dto.PedidoResponseDTO;
import com.pastelaria.familia.entity.Pedido;

public class PedidoMapper {

    public static Pedido paraEntidade(PedidoDTO dto) {
        if (dto == null) return null;

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(dto.usuarioId());
        pedido.setValorTotal(dto.valorTotal());
        pedido.setEnderecoEntrega(dto.enderecoEntrega());
        pedido.setFormaPagamento(dto.formaPagamento());
        return pedido;
    }

    public static PedidoResponseDTO paraResponseDTO(Pedido pedido) {
        if (pedido == null) return null;

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getUsuarioId(),
                pedido.getStatus(),
                pedido.getValorTotal(),
                pedido.getEnderecoEntrega(),
                pedido.getCriadoEm(),
                pedido.getFormaPagamento()
        );
    }
}