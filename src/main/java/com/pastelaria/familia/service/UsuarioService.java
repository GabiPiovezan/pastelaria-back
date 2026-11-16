package com.pastelaria.familia.service;

import com.pastelaria.familia.entity.Usuario;
import com.pastelaria.familia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario salvar(Usuario usuario) {
        if (usuario.getId() == null && usuario.getEmail() != null && usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }
        if (usuario.getSenha() != null && !usuario.getSenha().startsWith("$2a$") && !usuario.getSenha().startsWith("$2b$")) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario realizarLogin(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (usuario.getSenha() == null || senha == null) {
            throw new RuntimeException("Credenciais inválidas");
        }

        // Validação da senha com BCrypt
        if (usuario.getSenha().startsWith("$2a$") || usuario.getSenha().startsWith("$2b$")) {
            if (!passwordEncoder.matches(senha, usuario.getSenha())) {
                throw new RuntimeException("Credenciais inválidas");
            }
        } else {
            // Suporte a senhas em texto puro legadas, com migração automática para BCrypt no login
            if (!usuario.getSenha().equals(senha)) {
                throw new RuntimeException("Credenciais inválidas");
            }
            usuario.setSenha(passwordEncoder.encode(senha));
            usuario = usuarioRepository.save(usuario);
        }

        return usuario;
    }

    public void deletar(String id) {
        usuarioRepository.deleteById(id);
    }
}