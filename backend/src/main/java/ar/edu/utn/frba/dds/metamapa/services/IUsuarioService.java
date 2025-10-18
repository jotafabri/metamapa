package ar.edu.utn.frba.dds.metamapa.services;

import ar.edu.utn.frba.dds.metamapa.models.dtos.auth.RegistroRequest;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.UserDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Usuario;

public interface IUsuarioService {
  UserDTO register(RegistroRequest registroRequest);

  Usuario autenticar(String email, String password);

  UserDTO getUserByEmail(String email);

  String generarAccessToken(String email);

  String generarRefreshToken(String email);
}