package ar.edu.utn.frba.dds.metamapa.config;

import ar.edu.utn.frba.dds.metamapa.filters.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    log.info("Configurando Security (endpoints @PreAuthorize + públicos)");

    http
            // Deshabilitamos CSRF porque usamos JWT
            .csrf(AbstractHttpConfigurer::disable)

            // Sin sesión (autenticación stateless con JWT)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Configuración global de acceso
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.POST, "/auth/user").permitAll()

                    .requestMatchers("/hechos/me").hasAnyRole("USER","ADMIN")
                    .requestMatchers(HttpMethod.GET, "/hechos/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/hechos").permitAll()
                    .requestMatchers(HttpMethod.PATCH, "/hechos/**").hasAnyRole("USER","ADMIN")

                    .requestMatchers("/colecciones/admin/**").hasRole("ADMIN")
                    .requestMatchers("/colecciones/refrescar").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/files/**").permitAll()

                    .requestMatchers(HttpMethod.GET, "/colecciones/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/colecciones/**").hasRole( "ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/colecciones/**").hasRole( "ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/colecciones/**").hasRole( "ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/colecciones/**").hasRole("ADMIN")

                    .requestMatchers("/estadisticas/**").authenticated()

                    .requestMatchers(HttpMethod.GET, "/fuentes/**").permitAll()

                    .requestMatchers(
                            "/",
                            "/login",
                            "/register",
                            "/auth/login",
                            "/auth/registro",
                            "/auth/refresh").permitAll()

                    .requestMatchers("/graphql").permitAll()
                    .anyRequest().authenticated()
            )
            // Filtro JWT (se aplica si hay token, pero no obliga a tenerlo)
            .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}