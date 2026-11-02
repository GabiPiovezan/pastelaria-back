package com.pastelaria.familia.service;

import com.pastelaria.familia.entity.Entrega;
import com.pastelaria.familia.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    public List<Entrega> listarTodas() {
        return entregaRepository.findAll();
    }

    public Optional<Entrega> buscarPorId(String id) {
        return entregaRepository.findById(id);
    }

    public Optional<Entrega> buscarPorPedidoId(String pedidoId) {
        return entregaRepository.findByPedidoId(pedidoId);
    }

    public Entrega salvar(Entrega entrega) {
        return entregaRepository.save(entrega);
    }
}