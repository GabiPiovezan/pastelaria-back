package com.pastelaria.familia.controller;

import com.pastelaria.familia.dto.UsuarioDTO;
import com.pastelaria.familia.entity.Usuario;
import com.pastelaria.familia.mapper.UsuarioMapper;
import com.pastelaria.familia.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // 1. Listar todos os usuários cadastrados
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarTodos();
    }

    // 2. Cadastrar um novo usuário (Cliente)
    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody UsuarioDTO dto) {
        try {
            Usuario usuario = UsuarioMapper.paraEntidade(dto);
            // Garante que o cadastro público sempre registre 'cliente', prevenindo escalação indevida de privilégio
            usuario.setTipo("cliente");
            Usuario salvo = usuarioService.salvar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", e.getMessage()));
        }
    }

    // 3. Login de usuário existente
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody UsuarioDTO dto) {
        try {
            Usuario usuarioLogado = usuarioService.realizarLogin(dto.email(), dto.senha());
            return ResponseEntity.ok(usuarioLogado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // 4. Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable String id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 5. Atualizar usuário por ID
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable String id, @RequestBody Usuario usuarioDetalhes) {
        return usuarioService.buscarPorId(id).map(usuarioExistente -> {
            if (usuarioDetalhes.getNome() != null && !usuarioDetalhes.getNome().isBlank()) {
                usuarioExistente.setNome(usuarioDetalhes.getNome());
            }
            if (usuarioDetalhes.getEmail() != null && !usuarioDetalhes.getEmail().isBlank()) {
                usuarioExistente.setEmail(usuarioDetalhes.getEmail());
            }
            if (usuarioDetalhes.getTelefone() != null && !usuarioDetalhes.getTelefone().isBlank()) {
                usuarioExistente.setTelefone(usuarioDetalhes.getTelefone());
            }
            if (usuarioDetalhes.getSenha() != null && !usuarioDetalhes.getSenha().isBlank()) {
                usuarioExistente.setSenha(usuarioDetalhes.getSenha());
            }
            // Não permite que clientes alterem o campo 'tipo' para admin através deste endpoint
            Usuario atualizado = usuarioService.salvar(usuarioExistente);
            return ResponseEntity.ok(atualizado);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 6. Deletar usuário por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable String id) {
        if (usuarioService.buscarPorId(id).isPresent()) {
            usuarioService.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}