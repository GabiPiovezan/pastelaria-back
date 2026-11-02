package com.pastelaria.familia.repository;

import com.pastelaria.familia.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, String> {
    // Buscar todos os itens pertencentes a um pedido específico
    List<ItemPedido> findByPedidoId(String pedidoId);
}