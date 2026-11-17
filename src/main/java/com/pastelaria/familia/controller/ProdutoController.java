package com.pastelaria.familia.controller;

import com.pastelaria.familia.entity.Produto;
import com.pastelaria.familia.service.CloudinaryService;
import com.pastelaria.familia.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CloudinaryService cloudinaryService;

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
            if (produtoDetalhes.getImagemUrl() != null) {
                produto.setImagemUrl(produtoDetalhes.getImagemUrl());
            }

            Produto atualizado = produtoService.salvar(produto);
            return ResponseEntity.ok(atualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint dedicado para upload e vinculação de imagem do produto no Cloudinary
    @PostMapping(value = "/{id}/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Produto> uploadImagem(
            @PathVariable String id,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        MultipartFile arquivoParaUpload = (imagem != null && !imagem.isEmpty()) ? imagem : file;
        if (arquivoParaUpload == null || arquivoParaUpload.isEmpty()) {
            throw new IllegalArgumentException("Nenhum arquivo enviado no formulário (parâmetro 'imagem' ou 'file').");
        }

        Optional<Produto> produtoOpt = produtoService.buscarPorId(id);
        if (produtoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String secureUrl = cloudinaryService.uploadImagem(arquivoParaUpload);
        Produto produto = produtoOpt.get();
        produto.setImagemUrl(secureUrl);
        Produto atualizado = produtoService.salvar(produto);

        return ResponseEntity.ok(atualizado);
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