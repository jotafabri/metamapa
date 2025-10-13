package ar.edu.utn.frba.dds.metamapa_front.dtos;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String nombre;
  private String email;
  private String password;
}