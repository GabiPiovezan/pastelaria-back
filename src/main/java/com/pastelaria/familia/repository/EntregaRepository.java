package com.pastelaria.familia.repository;

import com.pastelaria.familia.entity.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, String> {
    // Método auxiliar para rastrear a entrega pelo ID do pedido
    Optional<Entrega> findByPedidoId(String pedidoId);
}