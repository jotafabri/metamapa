package ar.edu.utn.frba.dds.metamapa_front.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolesPermisosDTO {
  private String username;
  private Rol rol;
  private List<Permiso> permisos;
}
