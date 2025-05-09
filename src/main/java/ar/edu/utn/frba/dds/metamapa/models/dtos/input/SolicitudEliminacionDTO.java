package ar.edu.utn.frba.dds.metamapa.models.dtos.input;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import lombok.Data;

@Data
public class SolicitudEliminacionDTO {
  private HechoDTO hecho;
  private String razon;
}
