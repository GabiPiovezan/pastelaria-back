package com.pastelaria.familia.service;

import com.pastelaria.familia.entity.Produto;
import com.pastelaria.familia.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(String id) {
        return produtoRepository.findById(id);
    }

    public Produto salvar(Produto produto) {
        // Regra de negócio opcional: garantir que o preço seja válido
        if (produto.getPreco() != null && produto.getPreco().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço do produto não pode ser negativo.");
        }
        return produtoRepository.save(produto);
    }

    public void deletar(String id) {
        produtoRepository.deleteById(id);
    }
}