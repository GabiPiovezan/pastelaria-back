package com.pastelaria.familia.controller;

import com.pastelaria.familia.entity.Pedido;
import com.pastelaria.familia.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // 1. Listar todos os pedidos realizados
    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    // 2. Criar um novo pedido (Checkout)
    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
    }

    // 3. Buscar pedidos de um usuário específico
    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> listarPedidosPorUsuario(@PathVariable String usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }
}