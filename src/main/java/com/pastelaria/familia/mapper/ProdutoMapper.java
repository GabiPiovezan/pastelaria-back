package com.pastelaria.familia.mapper;

import com.pastelaria.familia.dto.ProdutoDTO;
import com.pastelaria.familia.dto.ProdutoResponseDTO;
import com.pastelaria.familia.entity.Produto;

public class ProdutoMapper {

    // Converte de RequestDTO para Entidade (para salvar no banco)
    public static Produto paraEntidade(ProdutoDTO dto) {
        if (dto == null) return null;

        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setCategoria(dto.categoria());
        produto.setDisponivel(dto.disponivel() != null ? dto.disponivel() : true);
        return produto;
    }

    // Converte de Entidade para ResponseDTO (para retornar ao cliente)
    public static ProdutoResponseDTO paraResponseDTO(Produto produto) {
        if (produto == null) return null;

        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getCategoria(),
                produto.getDisponivel()
        );
    }
}