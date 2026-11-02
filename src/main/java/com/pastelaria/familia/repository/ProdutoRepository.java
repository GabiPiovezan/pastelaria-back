package com.pastelaria.familia.repository;

import com.pastelaria.familia.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, String> {
    //métodos de CRUD (Salvar, Buscar por ID, Deletar, Listar Todos)
}