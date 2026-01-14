package ar.edu.utn.frba.dds.metamapa_front.providers;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa_front.dtos.AuthResponseDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.RolesDTO;
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

      log.info("Buscando rol del usuario");
      RolesDTO roles = externalAuthService.getRoles(authResponse.getAccessToken());

      log.info("Usuario autenticado. Guardando datos en sesión [email={}, rol={}]",
              roles.getEmail(), roles.getRol());

      request.getSession().setAttribute("username", roles.getEmail());
      request.getSession().setAttribute("rol", roles.getRol());

      List<GrantedAuthority> authorities = List.of(
              new SimpleGrantedAuthority("ROLE_" + roles.getRol().name())
      );

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
