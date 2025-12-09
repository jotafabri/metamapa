package ar.edu.utn.frba.dds.metamapa.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

  @Autowired
  private RateLimitService rateLimitService;

  @Override
  public boolean preHandle(HttpServletRequest request,
                          HttpServletResponse response,
                          Object handler) throws Exception {

    if (!(handler instanceof HandlerMethod)) {
      return true;
    }

    HandlerMethod handlerMethod = (HandlerMethod) handler;
    RateLimited rateLimited = handlerMethod.getMethodAnnotation(RateLimited.class);

    if (rateLimited == null) {
      rateLimited = handlerMethod.getBeanType().getAnnotation(RateLimited.class);
    }

    if (rateLimited != null) {
      String key = generateKey(request, rateLimited.type());
      boolean allowed = rateLimitService.allowRequest(
          key,
          rateLimited.maxRequests(),
          rateLimited.durationSeconds()
      );

      if (!allowed) {
        response.setStatus(429);
        response.setContentType("application/json");
        response.getWriter().write(
            "{\"error\": \"Demasiadas solicitudes\", "
            + "\"mensaje\": \"Límite de solicitudes excedido. Por favor, intente más tarde.\"}"
        );
        return false;
      }
    }

    return true;
  }

  private String generateKey(HttpServletRequest request, RateLimitType type) {
    return switch (type) {
      case IP_BASED -> getClientIp(request);
      case USER_BASED -> getUserIdentifier(request);
      case GLOBAL -> "global";
    };
  }

  private String getClientIp(HttpServletRequest request) {
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

  private String getUserIdentifier(HttpServletRequest request) {
    if (request.getUserPrincipal() != null) {
      return "user:" + request.getUserPrincipal().getName();
    }
    return getClientIp(request);
  }
}
