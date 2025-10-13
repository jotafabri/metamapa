package ar.edu.utn.frba.dds.metamapa.controllers;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.UsuarioDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.UserDTO;
import ar.edu.utn.frba.dds.metamapa.services.IUsuarioService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private IUsuarioService usuarioService;

  @PostMapping("/usuarios")
  public ResponseEntity<UserDTO> register(@RequestBody UsuarioDTO usuarioDTO) {
    try {
      UserDTO user = usuarioService.register(usuarioDTO);
      return ResponseEntity.ok(user);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/login")
  public ResponseEntity<UserDTO> login(@RequestBody UsuarioDTO usuarioDTO) {
    try {
      UserDTO user = usuarioService.login(usuarioDTO);
      return ResponseEntity.ok(user);
    } catch (RuntimeException e) {
      return ResponseEntity.status(401).build();
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