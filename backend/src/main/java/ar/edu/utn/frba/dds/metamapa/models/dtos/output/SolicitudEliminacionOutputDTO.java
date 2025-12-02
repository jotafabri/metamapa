package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.SolicitudEliminacion;
import lombok.Data;

@Data
public class SolicitudEliminacionOutputDTO {
  private Long id;
  private Long idHecho;
  private String razon;
  private String estado;
  private LocalDateTime fecha;

  // Mapper
  public static SolicitudEliminacionOutputDTO fromSolicitud(SolicitudEliminacion solicitud) {
    var dto = new SolicitudEliminacionOutputDTO();
    dto.setId(solicitud.getId());
    dto.setIdHecho(solicitud.getHecho().getId());
    dto.setRazon(solicitud.getCausa());
    dto.setEstado(solicitud.getEstado().name());
    dto.setFecha(solicitud.getFechaAlta());
    return dto;
  }
}
