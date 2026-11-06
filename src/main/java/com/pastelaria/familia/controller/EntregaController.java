package com.pastelaria.familia.controller;

import com.pastelaria.familia.entity.Entrega;
import com.pastelaria.familia.service.EntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/entregas")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    // 1. Listar todas as entregas (Painel do Administrador)
    @GetMapping
    public List<Entrega> listarEntregas() {
        return entregaService.listarTodas();
    }

    // 2. Criar um registro de entrega para um pedido
    @PostMapping
    public ResponseEntity<Entrega> criarEntrega(@RequestBody Entrega entrega) {
        Entrega novaEntrega = entregaService.salvar(entrega);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaEntrega);
    }

    // 3. Consultar o rastreio da entrega pelo ID do pedido (Cliente)
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Entrega> buscarPorPedidoId(@PathVariable String pedidoId) {
        Optional<Entrega> entrega = entregaService.buscarPorPedidoId(pedidoId);
        return entrega.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Atualizar o status da entrega
    @PutMapping("/{id}")
    public ResponseEntity<Entrega> atualizarStatusEntrega(@PathVariable String id, @RequestBody Entrega entregaAtualizada) {
        Optional<Entrega> entregaOpt = entregaService.buscarPorId(id);
        if (entregaOpt.isPresent()) {
            Entrega entrega = entregaOpt.get();
            entrega.setStatusEntrega(entregaAtualizada.getStatusEntrega());
            Entrega salva = entregaService.salvar(entrega);
            return ResponseEntity.ok(salva);
        }
        return ResponseEntity.notFound().build();
    }
}