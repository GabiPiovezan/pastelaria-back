package com.pastelaria.familia.service;

import com.pastelaria.familia.entity.ItemPedido;
import com.pastelaria.familia.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemPedidoService {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public List<ItemPedido> listarTodos() {
        return itemPedidoRepository.findAll();
    }

    public Optional<ItemPedido> buscarPorId(String id) {
        return itemPedidoRepository.findById(id);
    }

    public List<ItemPedido> listarPorPedidoId(String pedidoId) {
        return itemPedidoRepository.findByPedidoId(pedidoId);
    }

    public ItemPedido salvar(ItemPedido itemPedido) {
        // Regra de negócio opcional: garantir que a quantidade seja positiva
        if (itemPedido.getQuantidade() != null && itemPedido.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade do item deve ser maior do que zero.");
        }
        return itemPedidoRepository.save(itemPedido);
    }

    public void deletar(String id) {
        itemPedidoRepository.deleteById(id);
    }
}