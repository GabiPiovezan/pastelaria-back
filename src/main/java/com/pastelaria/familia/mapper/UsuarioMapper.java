package com.pastelaria.familia.mapper;

import com.pastelaria.familia.dto.UsuarioDTO;
import com.pastelaria.familia.dto.UsuarioResponseDTO;
import com.pastelaria.familia.entity.Usuario;

public class UsuarioMapper {
    public static Usuario paraEntidade(UsuarioDTO dto){
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(dto.senha()); // Será criptografada com BCrypt no service/controller
        usuario.setTipo(dto.tipo() != null ? dto.tipo() : "cliente");
        return usuario;
    }

    public static UsuarioResponseDTO paraResponseDTO(Usuario usuario) {
        if (usuario == null) return null;

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipo()
        );
    }
}
