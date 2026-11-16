package com.pastelaria.familia.controller;

import com.pastelaria.familia.entity.ItemPedido;
import com.pastelaria.familia.service.ItemPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itens-pedido")
public class ItemPedidoController {

    @Autowired
    private ItemPedidoService itemPedidoService;

    // Listar todos os itens de pedido
    @GetMapping
    public List<ItemPedido> listarTodos() {
        return itemPedidoService.listarTodos();
    }

    // Listar itens de um pedido específico
    @GetMapping("/pedido/{pedidoId}")
    public List<ItemPedido> listarItensPorPedido(@PathVariable String pedidoId) {
        return itemPedidoService.listarPorPedidoId(pedidoId);
    }

    // Buscar um item por ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemPedido> buscarPorId(@PathVariable String id) {
        return itemPedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Adicionar um item ao pedido
    @PostMapping
    public ResponseEntity<ItemPedido> adicionarItem(@RequestBody ItemPedido itemPedido) {
        ItemPedido salvo = itemPedidoService.salvar(itemPedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // Deletar um item por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarItem(@PathVariable String id) {
        if (itemPedidoService.buscarPorId(id).isPresent()) {
            itemPedidoService.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}