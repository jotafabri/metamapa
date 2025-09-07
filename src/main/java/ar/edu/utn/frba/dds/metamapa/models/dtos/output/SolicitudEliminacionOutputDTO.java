package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.SolicitudEliminacion;
import lombok.Data;

@Data
public class SolicitudEliminacionOutputDTO {
  private Long id;
  private HechoDTO hecho;
  private String razon;
  private String estado;

  // Mapper
  public static SolicitudEliminacionOutputDTO fromSolicitud(SolicitudEliminacion solicitud) {
    var dto = new SolicitudEliminacionOutputDTO();
    dto.setId(solicitud.getId());
    dto.setHecho(HechoDTO.fromHecho(solicitud.getHecho()));
    dto.setRazon(solicitud.getCausa());
    dto.setEstado(solicitud.getEstado().name());
    return dto;
  }
}
