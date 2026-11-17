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

        // Permite requisições OPTIONS (CORS preflight) passarem sem bloqueio
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Endpoints públicos de produtos: leituras via GET e busca retrocompatível por ID
        boolean isConsultaProdutoPorId = path.matches(".*/api/produtos/[^/]+$") && ("GET".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method));

        // Upload de imagem (/api/produtos/{id}/imagem) exige estritamente perfil de ADMIN
        boolean isUploadImagemProduto = path.matches(".*/api/produtos/[^/]+/imagem/?$") && "POST".equalsIgnoreCase(method);

        // Rotas administrativas de produtos: criação, atualização, exclusão e upload de imagem
        boolean isProtectedProdutoRoute = (path.contains("/api/produtos") && !"GET".equalsIgnoreCase(method) && !isConsultaProdutoPorId)
                || isUploadImagemProduto;

        // Rota de criação/modificação de categorias
        boolean isProtectedCategoriaRoute = path.contains("/api/categorias") && !"GET".equalsIgnoreCase(method);

        // Ações destrutivas ou administrativas em usuários, pedidos e entregas
        boolean isProtectedAdminAction = 
                (path.contains("/api/usuarios") && "DELETE".equalsIgnoreCase(method)) ||
                (path.contains("/api/pedidos") && "DELETE".equalsIgnoreCase(method)) ||
                (path.contains("/api/entregas") && ("DELETE".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)));

        boolean isProtected = isProtectedProdutoRoute || isProtectedCategoriaRoute || isProtectedAdminAction;

        if (isProtected) {
            String adminEmail = request.getHeader("X-Admin-Email");

            if (adminEmail == null || adminEmail.isBlank()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"erro\": \"Acesso negado: Cabecalho 'X-Admin-Email' ausente.\"}");
                return;
            }

            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(adminEmail);

            if (usuarioOpt.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"erro\": \"Acesso negado: Usuario nao encontrado.\"}");
                return;
            }

            Usuario usuario = usuarioOpt.get();

            String tipo = usuario.getTipo();
            if (tipo == null || (!"admin".equalsIgnoreCase(tipo) && !"administrador".equalsIgnoreCase(tipo))) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"erro\": \"Acesso negado: Apenas administradores podem realizar esta acao.\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}