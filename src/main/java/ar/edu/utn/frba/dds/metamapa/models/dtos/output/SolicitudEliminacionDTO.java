package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;

@Data
public class SolicitudEliminacionDTO {
  private Hecho hecho;
  private String razon;
  
}
