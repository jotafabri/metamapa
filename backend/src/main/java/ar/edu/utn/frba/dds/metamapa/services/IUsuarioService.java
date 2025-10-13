package ar.edu.utn.frba.dds.metamapa.services;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.UsuarioDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.UserDTO;

public interface IUsuarioService {
  UserDTO register(UsuarioDTO usuarioDTO);
  UserDTO login(UsuarioDTO usuarioDTO);
  UserDTO getUserByEmail(String email);
}