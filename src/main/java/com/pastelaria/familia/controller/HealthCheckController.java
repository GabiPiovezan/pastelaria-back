package com.pastelaria.familia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<?> verificarSaude() {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", "UP");
        resposta.put("mensagem", "API da Pastelaria-Família & Cia está operando normalmente! 🥐");
        resposta.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(resposta);
    }
}