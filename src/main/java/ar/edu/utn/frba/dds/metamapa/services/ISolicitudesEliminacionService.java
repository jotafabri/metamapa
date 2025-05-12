package ar.edu.utn.frba.dds.metamapa.services;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.SolicitudEliminacion;

public interface ISolicitudesEliminacionService {

  public void crear_solicitudDTO(SolicitudEliminacionDTO solicitudDTO);
  public void aprobarSolicitud(SolicitudEliminacion solicitud);
  public void rechazarSolicitud(SolicitudEliminacion solicitud);
}
