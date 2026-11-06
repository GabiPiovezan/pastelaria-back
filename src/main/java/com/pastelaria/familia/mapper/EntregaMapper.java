package com.pastelaria.familia.mapper;

import com.pastelaria.familia.dto.EntregaDTO;
import com.pastelaria.familia.dto.EntregaResponseDTO;
import com.pastelaria.familia.entity.Entrega;

public class EntregaMapper {

    public static Entrega paraEntidade(EntregaDTO dto) {
        if (dto == null) return null;

        Entrega entrega = new Entrega();
        entrega.setPedidoId(dto.pedidoId());
        entrega.setStatusEntrega(dto.statusEntrega());
        return entrega;
    }

    public static EntregaResponseDTO paraResponseDTO(Entrega entrega) {
        if (entrega == null) return null;

        return new EntregaResponseDTO(
                entrega.getId(),
                entrega.getPedidoId(),
                entrega.getStatusEntrega(),
                entrega.getAtualizadoEm()
        );
    }
}