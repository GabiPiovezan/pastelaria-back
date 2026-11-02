package com.pastelaria.familia.repository;

import com.pastelaria.familia.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    // Método auxiliar para buscar usuário por e-mail (muito útil para o login futuramente)
    Optional<Usuario> findByEmail(String email);
}