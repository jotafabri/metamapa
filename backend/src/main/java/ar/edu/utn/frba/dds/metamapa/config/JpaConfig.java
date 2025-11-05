package ar.edu.utn.frba.dds.metamapa.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();

      if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
        // Durante el seeding o procesos sin usuario autenticado
        return Optional.of("system");
      }

      return Optional.of(auth.getName());
    };
  }
}
