package ar.edu.utn.frba.dds.metamapa.filters;

import java.io.IOException;
import java.util.Collections;

import ar.edu.utn.frba.dds.metamapa.models.entities.Usuario;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IUsuarioRepository;
import ar.edu.utn.frba.dds.metamapa.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {


  private final IUsuarioRepository usuarioRepository;

  public JwtAuthenticationFilter(IUsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
          throws ServletException, IOException {

    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      System.out.println("Header Authorization: " + header);
      System.out.println("Token recibido en el filtro: " + token);

      try {
        // 🔹 Validamos el token y obtenemos el email o username
        String username = JwtUtil.validarToken(token);

        // 🔹 Buscamos al usuario en la base
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        // 🔹 Recuperamos el rol real
        String rol = usuario.getRol().name(); // Ej: "ADMIN" o "USER"

        // 🔹 Creamos el objeto de autenticación con su rol
        var auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol))
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        System.out.println("Usuario autenticado: " + username + " | Rol: " + rol);

      } catch (Exception e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o usuario no encontrado");
        return;
      }

    } else {
      System.out.println("No hay token de autorización");
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    // No aplicar el filtro JWT solo a los endpoints públicos de autenticación
    return path.equals("/auth/login")
            || path.equals("/auth/registro")
            || path.equals("/auth/refresh");
  }
}
