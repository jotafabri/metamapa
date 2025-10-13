package ar.edu.utn.frba.dds.metamapa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // Desactivar CSRF para desarrollo
        .authorizeHttpRequests(auth -> auth
            // Permitir acceso público a endpoints de autenticación
            .requestMatchers("/api/auth/**").permitAll()
            // Permitir acceso a otros endpoints (temporal para desarrollo)
            .anyRequest().permitAll()
        );

    return http.build();
  }
}