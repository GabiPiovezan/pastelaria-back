package com.pastelaria.familia.dto;

public record UsuarioResponseDTO(
        String id,
        String nome,
        String email,
        String tipo
) {}