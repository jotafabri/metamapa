package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import ar.edu.utn.frba.dds.metamapa.models.entities.Usuario;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Permiso;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Rol;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
  private Long id;
  private String email;
  private Rol rol;
  private List<Permiso> permisos;

  public static UserDTO fromUsuario(Usuario usuario) {
    UserDTO dto = new UserDTO();
    dto.setId(usuario.getId());
    dto.setEmail(usuario.getEmail());
    dto.setRol(usuario.getRol());
    dto.setPermisos(usuario.getPermisos());
    return dto;
  }
}