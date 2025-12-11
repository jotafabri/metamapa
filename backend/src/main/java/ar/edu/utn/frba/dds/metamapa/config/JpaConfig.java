package ar.edu.utn.frba.dds.metamapa.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> {
      SecurityContext context = SecurityContextHolder.getContext();
      if (context == null || context.getAuthentication() == null) {
        return Optional.of("SYSTEM"); // ← Usuario por defecto para schedulers
      }
      return Optional.ofNullable(context.getAuthentication().getName());
    };
  }

}


