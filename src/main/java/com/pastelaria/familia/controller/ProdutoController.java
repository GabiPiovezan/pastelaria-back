package com.pastelaria.familia.controller;

import com.pastelaria.familia.entity.Produto;
import com.pastelaria.familia.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos(@RequestParam(required = false) String categoria) {
        if (categoria != null && !categoria.isEmpty()){
            List<Produto> produtosPorCategoria = produtoService.listarPorCategoria(categoria);
            return ResponseEntity.ok(produtosPorCategoria);
        }
        List<Produto> todosOsProdutos = produtoService.listarTodos();
        return ResponseEntity.ok(todosOsProdutos);
    }

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.salvar(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    // Suporta GET e POST para buscar por ID, garantindo compatibilidade retroativa com o frontend
    @RequestMapping(value = "/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Produto> buscarPorId(@PathVariable String id) {
        Optional<Produto> produto = produtoService.buscarPorId(id);
        return produto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable String id, @RequestBody Produto produtoDetalhes) {
        Optional<Produto> produtoOpt = produtoService.buscarPorId(id);
        if (produtoOpt.isPresent()) {
            Produto produto = produtoOpt.get();
            produto.setNome(produtoDetalhes.getNome());
            produto.setDescricao(produtoDetalhes.getDescricao());
            produto.setPreco(produtoDetalhes.getPreco());
            produto.setCategoria(produtoDetalhes.getCategoria());
            produto.setDisponivel(produtoDetalhes.getDisponivel());

            Produto atualizado = produtoService.salvar(produto);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable String id) {
        if (produtoService.buscarPorId(id).isPresent()) {
            produtoService.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}