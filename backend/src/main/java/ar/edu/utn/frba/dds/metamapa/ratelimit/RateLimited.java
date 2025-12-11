package ar.edu.utn.frba.dds.metamapa.ratelimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {

  int maxRequests() default 100;

  int durationSeconds() default 60;

  RateLimitType type() default RateLimitType.IP_BASED;

}
