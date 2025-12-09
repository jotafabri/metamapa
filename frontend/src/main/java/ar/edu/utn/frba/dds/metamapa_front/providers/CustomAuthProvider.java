package ar.edu.utn.frba.dds.metamapa_front.providers;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa_front.dtos.AuthResponseDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.RolesPermisosDTO;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.RateLimitExceededException;
import ar.edu.utn.frba.dds.metamapa_front.services.MetamapaApiService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class CustomAuthProvider implements AuthenticationProvider {
  private static final Logger log = LoggerFactory.getLogger(CustomAuthProvider.class);
  private final MetamapaApiService externalAuthService;

  public CustomAuthProvider(MetamapaApiService externalAuthService) {
    this.externalAuthService = externalAuthService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    try {
      // Llamada a servicio externo para obtener tokens
      AuthResponseDTO authResponse = externalAuthService.login(username, password);

      if (authResponse == null) {
        throw new BadCredentialsException("Usuario o contraseña inválidos");
      }

      log.info("Usuario logeado! Configurando variables de sesión");
      ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
      HttpServletRequest request = attributes.getRequest();

      request.getSession().setAttribute("accessToken", authResponse.getAccessToken());
      request.getSession().setAttribute("refreshToken", authResponse.getRefreshToken());
      request.getSession().setAttribute("username", username);

      log.info("Buscando roles y permisos del usuario");
      RolesPermisosDTO rolesPermisos = externalAuthService.getRolesPermisos(username);

      log.info("Cargando roles y permisos del usuario en sesión");
      request.getSession().setAttribute("rol", rolesPermisos.getRol());
      request.getSession().setAttribute("permisos", rolesPermisos.getPermisos());

      List<GrantedAuthority> authorities = new ArrayList<>();
      rolesPermisos.getPermisos().forEach(permiso -> {
        authorities.add(new SimpleGrantedAuthority(permiso.name()));
      });
      authorities.add(new SimpleGrantedAuthority("ROLE_" + rolesPermisos.getRol().name()));

      return new UsernamePasswordAuthenticationToken(username, password, authorities);

    } catch (RuntimeException e) {
      if (e.getMessage() != null && e.getMessage().contains("RATE_LIMIT_EXCEEDED")) {
        throw new RateLimitExceededException("Demasiados intentos de inicio de sesión. Por favor, intenta de nuevo en unos minutos.");
      }
      throw new BadCredentialsException("Error en el sistema de autenticación: " + e.getMessage());
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
