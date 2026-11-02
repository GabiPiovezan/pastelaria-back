package com.pastelaria.familia.controller;

import com.pastelaria.familia.entity.ItemPedido;
import com.pastelaria.familia.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itens-pedido")
public class ItemPedidoController {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    // Listar itens de um pedido específico
    @GetMapping("/pedido/{pedidoId}")
    public List<ItemPedido> listarItensPorPedido(@PathVariable String pedidoId) {
        return itemPedidoRepository.findByPedidoId(pedidoId);
    }

    // Adicionar um item ao pedido
    @PostMapping
    public ResponseEntity<ItemPedido> adicionarItem(@RequestBody ItemPedido itemPedido) {
        ItemPedido salvo = itemPedidoRepository.save(itemPedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}