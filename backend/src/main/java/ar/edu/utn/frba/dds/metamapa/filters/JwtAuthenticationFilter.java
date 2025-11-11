package ar.edu.utn.frba.dds.metamapa.filters;

import java.io.IOException;
import java.util.Collections;

import ar.edu.utn.frba.dds.metamapa.utils.JwtUtil;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      log.info("JWT recibido: {}", token);
      try {
        String username = JwtUtil.validarToken(token);
        String rol = Jwts.parserBuilder()
                .setSigningKey(JwtUtil.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("rol", String.class);

        log.info("Token válido. Usuario: {}, Rol: {}", username, rol);

        var auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        log.info("Autenticación asignada al contexto de seguridad para {}", username);

      } catch (Exception e) {
        log.error("Error al validar token: {}", e.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
        return;
      }
    } else {
      log.warn("Solicitud a '{}' no tiene token de autorización. Si el endpoint requiere auth, se denegará acceso.", request.getRequestURI());

    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    // Ignorar todos los endpoints de autenticación pública
    return path.startsWith("/auth");
  }
}
