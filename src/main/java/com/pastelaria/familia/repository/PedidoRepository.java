package com.pastelaria.familia.repository;

import com.pastelaria.familia.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, String> {
    // Método auxiliar para buscar pedidos de um usuário específico
    List<Pedido> findByUsuarioId(String usuarioId);
}