

package ar.edu.utn.frba.dds.metamapa.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
public class IpSecurityFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(IpSecurityFilter.class);

  @Autowired
  private IpFilterService ipFilterService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    String clientIp = obtenerIpCliente(request);

    if (!ipFilterService.esIpPermitida(clientIp)) {
      log.warn("Acceso bloqueado desde IP: {} - URI: {}", clientIp, request.getRequestURI());
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json");
      response.getWriter().write(
          "{\"error\": \"Acceso denegado\", "
          + "\"mensaje\": \"Tu dirección IP ha sido bloqueada por razones de seguridad.\"}"
      );
      return;
    }

    filterChain.doFilter(request, response);
  }

  private String obtenerIpCliente(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0].trim();
    }

    String xRealIp = request.getHeader("X-Real-IP");
    if (xRealIp != null && !xRealIp.isEmpty()) {
      return xRealIp;
    }

    return request.getRemoteAddr();
  }
}
