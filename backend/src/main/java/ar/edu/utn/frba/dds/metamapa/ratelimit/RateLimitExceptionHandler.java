package ar.edu.utn.frba.dds.metamapa.ratelimit;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RateLimitExceptionHandler {

  @ExceptionHandler(RateLimitExceededException.class)
  public ResponseEntity<Map<String, Object>> handleRateLimitExceeded(
      RateLimitExceededException ex) {

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("error", "Demasiadas solicitudes");
    errorResponse.put("mensaje", ex.getMessage());
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.TOO_MANY_REQUESTS.value());

    return ResponseEntity
        .status(HttpStatus.TOO_MANY_REQUESTS)
        .body(errorResponse);
  }
}
