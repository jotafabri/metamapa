package ar.edu.utn.frba.dds.metamapa.config;

import ar.edu.utn.frba.dds.metamapa.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IUsuarioRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final IUsuarioRepository usuarioRepository;

    public SecurityConfig(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(usuarioRepository);
    }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    System.out.println("=== CONFIGURANDO SECURITY ===");

    http.csrf(AbstractHttpConfigurer::disable) // Desactivar CSRF para desarrollo
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth ->  {

          // Permitir acceso público a endpoints de autenticación
            auth.requestMatchers("/auth/login","/auth/registro","/auth/refresh").permitAll(); //PERMITE HACER EL LOGIN Y REFRESHTOKEN
            auth.requestMatchers("/auth/user/roles-permisos").authenticated(); //EXIJO QUE ESTE AUTENTICADO
            auth.anyRequest().authenticated();
        })
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}