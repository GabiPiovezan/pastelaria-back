package com.pastelaria.familia.repository;

import com.pastelaria.familia.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, String> {

    List<Produto> findByCategoriaContainingIgnoreCase(String categoria);
}