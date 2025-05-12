package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import lombok.Data;

@Data
public class SolicitudEliminacionDTO {
  private Hecho hecho;
  private String causa;
  
}
