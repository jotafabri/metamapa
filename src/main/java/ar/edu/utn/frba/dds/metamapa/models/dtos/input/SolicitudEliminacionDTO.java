package ar.edu.utn.frba.dds.metamapa.models.dtos.input;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.SolicitudEliminacion;
import lombok.Data;

@Data
public class SolicitudEliminacionDTO {
  private Long idHecho;
  private String razon;

  // Mapper
  public static SolicitudEliminacionDTO fromSolicitud(SolicitudEliminacion solicitud) {
    var dto = new SolicitudEliminacionDTO();
    dto.setIdHecho(solicitud.getHecho().getId());
    dto.setRazon(solicitud.getCausa());
    return dto;
  }
}
