package com.pastelaria.familia.service;

import com.pastelaria.familia.entity.Categoria;
import com.pastelaria.familia.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodas(){
        return categoriaRepository.findAll();
    }

    public Categoria salvar(Categoria categoria){
        return categoriaRepository.save(categoria);
    }
}
