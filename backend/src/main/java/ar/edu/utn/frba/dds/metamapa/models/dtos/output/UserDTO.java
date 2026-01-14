package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import ar.edu.utn.frba.dds.metamapa.models.entities.Usuario;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Rol;
import lombok.Data;

@Data
public class UserDTO {
  private Long id;
  private String nombre;
  private String email;
  private Rol rol;

  public static UserDTO fromUsuario(Usuario usuario) {
    UserDTO dto = new UserDTO();
    dto.setId(usuario.getId());
    dto.setEmail(usuario.getEmail());
    dto.setRol(usuario.getRol());
    return dto;
  }

  public boolean esAdmin() {
    return this.rol == Rol.ADMIN;
  }

}