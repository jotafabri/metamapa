package ar.edu.utn.frba.dds.metamapa.models.dtos.auth;

import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Permiso;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
  private String accessToken;
  private String refreshToken;
  private Rol rol;              // rol del usuario
  private List<Permiso> permisos;  // permisos del usuario
}
