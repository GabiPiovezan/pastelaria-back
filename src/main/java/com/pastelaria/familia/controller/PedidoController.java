package com.pastelaria.familia.controller;

import com.pastelaria.familia.dto.PedidoDTO;
import com.pastelaria.familia.entity.Pedido;
import com.pastelaria.familia.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable String id) {
        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> listarPedidosPorUsuario(@PathVariable String usuarioId) {
        return pedidoService.listarPorUsuario(usuarioId);
    }

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@Valid @RequestBody PedidoDTO dto) {
        Pedido novoPedido = pedidoService.criarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable String id, @RequestBody Pedido dados) {
        return pedidoService.buscarPorId(id).map(pedidoExistente -> {
            if (dados.getStatus() != null) {
                pedidoExistente.setStatus(dados.getStatus());
            }
            if (dados.getFormaPagamento() != null) {
                pedidoExistente.setFormaPagamento(dados.getFormaPagamento());
            }
            if (dados.getEnderecoEntrega() != null) {
                pedidoExistente.setEnderecoEntrega(dados.getEnderecoEntrega());
            }
            if (dados.getTipoEntrega() != null) {
                pedidoExistente.setTipoEntrega(dados.getTipoEntrega());
            }
            if (dados.getTelefone() != null) {
                pedidoExistente.setTelefone(dados.getTelefone());
            }
            Pedido atualizado = pedidoService.salvar(pedidoExistente);
            return ResponseEntity.ok(atualizado);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Pedido> cancelarPedido(@PathVariable String id) {
        return pedidoService.buscarPorId(id).map(pedidoExistente -> {
            pedidoExistente.setStatus("cancelado");
            Pedido atualizado = pedidoService.salvar(pedidoExistente);
            return ResponseEntity.ok(atualizado);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable String id) {
        if (pedidoService.buscarPorId(id).isPresent()) {
            pedidoService.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}