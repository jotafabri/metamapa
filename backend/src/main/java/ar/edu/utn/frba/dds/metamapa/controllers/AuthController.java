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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequest loginRequest) {
    try {
      String email = loginRequest.getEmail();
      String password = loginRequest.getPassword();

      if (email == null || email.trim().isEmpty() ||
              password == null || password.trim().isEmpty()) {
        return ResponseEntity.badRequest().build();
      }

      Usuario usuario = usuarioService.autenticar(email, password);

      String accessToken = usuarioService.generarAccessToken(usuario.getEmail(), usuario.getRol().name());
      String refreshToken = usuarioService.generarRefreshToken(usuario.getEmail());

      AuthResponseDTO response = AuthResponseDTO.builder()
              .accessToken(accessToken)
              .refreshToken(refreshToken)
              .build();

      log.info("El usuario {} está logueado. Token generado: {}", email, accessToken);

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

      Claims claims = Jwts.parserBuilder()
              .setSigningKey(JwtUtil.getKey())
              .build()
              .parseClaimsJws(request.getRefreshToken())
              .getBody();

      if (!"refresh".equals(claims.get("type"))) {
        return ResponseEntity.badRequest().build();
      }

      // obtener el rol del usuario (de la BD)
      Usuario usuario = usuarioService.getUserEntityByEmail(email);

      String newAccessToken = JwtUtil.generarAccessToken(email, usuario.getRol().name());
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
}