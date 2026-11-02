package com.pastelaria.familia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    // Relação N:1 - Muitos pedidos podem pertencer a um usuário
    @Column(name = "usuario_id", nullable = false, length = 36)
    private String usuarioId;

    @Column(nullable = false, length = 30)
    private String status = "pendente"; // pendente, em_preparo, saindo_para_entrega, entregue, cancelado

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "endereco_entrega", nullable = false, columnDefinition = "TEXT")
    private String enderecoEntrega;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.criadoEm == null) {
            this.criadoEm = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "pendente";
        }
    }
}