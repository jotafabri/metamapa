package ar.edu.utn.frba.dds.metamapa.controllers;

import java.util.Map;

import ar.edu.utn.frba.dds.metamapa.exceptions.DuplicateEmailException;
import ar.edu.utn.frba.dds.metamapa.models.dtos.auth.AuthResponseDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.auth.LoginRequest;
import ar.edu.utn.frba.dds.metamapa.models.dtos.auth.RefreshRequest;
import ar.edu.utn.frba.dds.metamapa.models.dtos.auth.RegistroRequest;
import ar.edu.utn.frba.dds.metamapa.models.dtos.auth.TokenResponse;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.UserDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Usuario;
import ar.edu.utn.frba.dds.metamapa.services.IUsuarioService;
import ar.edu.utn.frba.dds.metamapa.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

  private static final Logger log = LoggerFactory.getLogger(AuthController.class);


  @Autowired
  private IUsuarioService usuarioService;

  @PostMapping("/registro")
  public ResponseEntity<UserDTO> register(@RequestBody RegistroRequest registroRequest) {
    try {
      UserDTO user = usuarioService.register(registroRequest);
      return ResponseEntity.ok(user);
    } catch (DuplicateEmailException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> loginApi(@RequestBody Map<String, String> credentials) {
    try {
      String username = credentials.get("email");
      String password = credentials.get("password");

      log.info("Intento de login recibido - email: '{}', password: '{}'", username, password != null ? "****" : null);

      // Validación básica de credenciales
      if (username == null || username.trim().isEmpty() ||
          password == null || password.trim().isEmpty()) {
        log.warn("Credenciales vacías recibidas");
        return ResponseEntity.badRequest().build();
      }
      //Autenticar el usuario usando el LoginService
      Usuario usuario = usuarioService.autenticar(username, password);
      log.info("Usuario autenticado: email='{}', rol='{}'", usuario.getEmail(), usuario.getRol());

      // Generar tokens
      String accessToken = usuarioService.generarAccessToken(username);
      String refreshToken = usuarioService.generarRefreshToken(username);

      AuthResponseDTO response = AuthResponseDTO.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
              .rol(usuario.getRol())                  // <-- agregamos rol
              .permisos(usuario.getPermisos())        // <-- agregamos permisos
          .build();

      log.info("El usuario {} está logueado. El token generado es {}", username, accessToken);

      return ResponseEntity.ok(response);
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(401).build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
    try {
      String email = JwtUtil.validarToken(request.getRefreshToken());

      // Validar que el token sea de tipo refresh
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(JwtUtil.getKey())
          .build()
          .parseClaimsJws(request.getRefreshToken())
          .getBody();

      if (!"refresh".equals(claims.get("type"))) {
        return ResponseEntity.badRequest().build();
      }

      String newAccessToken = JwtUtil.generarAccessToken(email);
      TokenResponse response = new TokenResponse(newAccessToken, request.getRefreshToken());

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/user")
  public ResponseEntity<UserDTO> getUserByEmail(@RequestBody Map<String, String> body) {
    try {
      String email = body.get("email");
      UserDTO user = usuarioService.getUserByEmail(email);
      return ResponseEntity.ok(user);
    } catch (RuntimeException e) {
      return ResponseEntity.status(404).build();
    }
  }

  @GetMapping("/user/roles-permisos")
  public ResponseEntity<AuthResponseDTO> getRolesPermisos(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String email = authentication.getName(); // llenado por JwtAuthenticationFilter
    UserDTO usuario = usuarioService.getUserByEmail(email);

    AuthResponseDTO dto = AuthResponseDTO.builder()
            .rol(usuario.getRol())
            .permisos(usuario.getPermisos())
            .accessToken("") // no hace falta devolverlo aquí
            .refreshToken("")
            .build();

    return ResponseEntity.ok(dto);
  }

}