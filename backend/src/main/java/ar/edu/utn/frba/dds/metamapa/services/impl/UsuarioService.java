package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.exceptions.DuplicateEmailException;
import ar.edu.utn.frba.dds.metamapa.models.dtos.auth.RegistroRequest;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.UserDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Usuario;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Rol;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IUsuarioRepository;
import ar.edu.utn.frba.dds.metamapa.services.IUsuarioService;
import ar.edu.utn.frba.dds.metamapa.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements IUsuarioService {

  @Autowired
  private IUsuarioRepository usuarioRepository;

  private final BCryptPasswordEncoder passwordEncoder;

  public UsuarioService() {
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  @Override
  public UserDTO register(RegistroRequest registroRequest) {
    if (usuarioRepository.existsByEmail(registroRequest.getEmail())) {
      throw new DuplicateEmailException(registroRequest.getEmail());
    }

    Usuario usuario = Usuario.builder()
        .email(registroRequest.getEmail())
        .password(passwordEncoder.encode(registroRequest.getPassword()))
        .rol(Rol.USER)
        .nombre(registroRequest.getNombre())
        .apellido(registroRequest.getApellido())
        .fechaNacimiento(registroRequest.getFechaNacimiento())
        .build();
    usuarioRepository.save(usuario);

    return UserDTO.fromUsuario(usuario);
  }

  @Override
  public Usuario autenticar(String email, String password) {
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

    if (!passwordEncoder.matches(password, usuario.getPassword())) {
      throw new BadCredentialsException("Credenciales inválidas");
    }

    return usuario;
  }

  public String generarAccessToken(String email, String rol) {
    return JwtUtil.generarAccessToken(email, rol);
  }

  public String generarRefreshToken(String email) {
    return JwtUtil.generarRefreshToken(email);
  }

  @Override
  public UserDTO getUserByEmail(String email) {
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    return UserDTO.fromUsuario(usuario);
  }

  public void cambiarRol(Long usuarioId, Rol rol) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    usuario.setRol(rol);
    usuarioRepository.save(usuario);
  }

  public void cambiarPassword(Long usuarioId, String nuevaPassword) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    usuario.setPassword(passwordEncoder.encode(nuevaPassword));
    usuarioRepository.save(usuario);
  }

  public Usuario getUserEntityByEmail(String email) {
    return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
  }

}