package ar.edu.utn.frba.dds.metamapa_front.exceptions;

import org.springframework.security.core.AuthenticationException;

public class RateLimitExceededException extends AuthenticationException {

  public RateLimitExceededException(String message) {
    super(message);
  }

  public RateLimitExceededException(String message, Throwable cause) {
    super(message, cause);
  }
}
