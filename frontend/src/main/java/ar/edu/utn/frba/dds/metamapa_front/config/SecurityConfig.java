package ar.edu.utn.frba.dds.metamapa_front.config;

import ar.edu.utn.frba.dds.metamapa_front.providers.CustomAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

  @Bean
  public AuthenticationManager authManager(HttpSecurity http, CustomAuthProvider provider) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(provider)
        .build();
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/hechos/crear", "/register")
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/admin/login").permitAll()
            .requestMatchers("/admin/**").hasAnyRole("ADMIN")
            .requestMatchers("/hechos/me").authenticated()
                .requestMatchers("/hechos/*/editar").authenticated()
            .anyRequest().permitAll()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .usernameParameter("email")
            .passwordParameter("password")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout") // redirigir tras logout
            .permitAll()
        )
        .exceptionHandling(ex -> ex
            // Usuario no autenticado → redirigir a login
            .authenticationEntryPoint((request, response, authException) -> {
                  if (request.getRequestURI().startsWith("/admin")) {
                    response.sendRedirect("/admin/login?unauthorized");
                  } else {
                    response.sendRedirect("/login?unauthorized");
                  }
                }
            )
            // Usuario autenticado pero sin permisos → redirigir a página de error
            .accessDeniedHandler((request, response, accessDeniedException) ->
                response.sendRedirect("/403")
            )
        );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
