package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.UsuarioDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.UserDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Usuario;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Rol;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IUsuarioRepository;
import ar.edu.utn.frba.dds.metamapa.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements IUsuarioService {

  @Autowired
  private IUsuarioRepository usuarioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  // Método privado base para crear usuarios
  private Usuario crearUsuario(String email, String password, Rol rol) {
    if (usuarioRepository.existsByEmail(email)) {
      throw new RuntimeException("El email ya está registrado");
    }

    String hashedPassword = passwordEncoder.encode(password);
    Usuario usuario = new Usuario(email, hashedPassword, rol);
    return usuarioRepository.save(usuario);
  }

  @Override
  public UserDTO register(UsuarioDTO usuarioDTO) {
    Usuario usuario = crearUsuario(usuarioDTO.getEmail(), usuarioDTO.getPassword(), Rol.USER);
    return UserDTO.fromUsuario(usuario);
  }

  @Override
  public UserDTO login(UsuarioDTO usuarioDTO) {
    Usuario usuario = usuarioRepository.findByEmail(usuarioDTO.getEmail())
        .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

    if (!passwordEncoder.matches(usuarioDTO.getPassword(), usuario.getPassword())) {
      throw new RuntimeException("Credenciales inválidas");
    }

    return UserDTO.fromUsuario(usuario);
  }

  @Override
  public UserDTO getUserByEmail(String email) {
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    return UserDTO.fromUsuario(usuario);
  }

  // Métodos adicionales de gestión de usuarios
  public Usuario crear(String email, String password, Rol rol) {
    return crearUsuario(email, password, rol != null ? rol : Rol.USER);
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

  public UserDTO autenticar(UsuarioDTO usuarioDTO) {
    return this.login(usuarioDTO);
  }

}