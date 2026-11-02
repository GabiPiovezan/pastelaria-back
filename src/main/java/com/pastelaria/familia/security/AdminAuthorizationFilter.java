package com.pastelaria.familia.security;

import com.pastelaria.familia.entity.Usuario;
import com.pastelaria.familia.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class AdminAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        boolean isProtectedProdutoRoute = path.startsWith("/api/produtos") && !method.equals("GET");

        if (isProtectedProdutoRoute) {
            String adminEmail = request.getHeader("X-Admin-Email");
            System.out.println("DEBUG - Rota protegida acessada. Email recebido no header: " + adminEmail);

            if (adminEmail == null) {
                System.out.println("DEBUG - Falha: Cabeçalho 'X-Admin-Email' ausente.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Acesso negado: Cabecalho 'X-Admin-Email' ausente.");
                return;
            }

            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(adminEmail);

            if (usuarioOpt.isEmpty()) {
                System.out.println("DEBUG - Falha: Usuário não encontrado no banco com o email: " + adminEmail);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Acesso negado: Usuario nao encontrado.");
                return;
            }

            Usuario usuario = usuarioOpt.get();
            System.out.println("DEBUG - Usuário encontrado. Tipo no banco: " + usuario.getTipo());

            if (!"admin".equalsIgnoreCase(usuario.getTipo())) {
                System.out.println("DEBUG - Falha: O usuário não é admin (tipo é: " + usuario.getTipo() + ")");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Acesso negado: Apenas administradores podem realizar esta acao.");
                return;
            }

            System.out.println("DEBUG - Sucesso! Acesso de administrador autorizado.");
        }

        filterChain.doFilter(request, response);
    }
}