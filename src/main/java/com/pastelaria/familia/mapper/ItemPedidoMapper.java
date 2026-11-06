package com.pastelaria.familia.mapper;

import com.pastelaria.familia.dto.ItemPedidoDTO;
import com.pastelaria.familia.dto.ItemPedidoResponseDTO;
import com.pastelaria.familia.entity.ItemPedido;

public class ItemPedidoMapper {

    public static ItemPedido paraEntidade(ItemPedidoDTO dto) {
        if (dto == null) return null;

        ItemPedido item = new ItemPedido();
        item.setPedidoId(dto.pedidoId());
        item.setProdutoId(dto.produtoId());
        item.setQuantidade(dto.quantidade());
        item.setPrecoUnitario(dto.precoUnitario());
        return item;
    }

    public static ItemPedidoResponseDTO paraResponseDTO(ItemPedido item) {
        if (item == null) return null;

        return new ItemPedidoResponseDTO(
                item.getId(),
                item.getPedidoId(),
                item.getProdutoId(),
                item.getQuantidade(),
                item.getPrecoUnitario()
        );
    }
}