package ar.edu.utn.frba.dds.metamapa.models.dtos.input;

import lombok.Data;

@Data
public class UsuarioDTO {
  private String email;
  private String password;
}