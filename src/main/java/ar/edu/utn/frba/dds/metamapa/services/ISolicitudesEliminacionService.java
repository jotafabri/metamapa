package ar.edu.utn.frba.dds.metamapa.services;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.SolicitudEliminacion;

public interface ISolicitudesEliminacionService {

  void crearSolicitud(SolicitudEliminacionDTO solicitud);

  void aprobarSolicitud(SolicitudEliminacion solicitud);

  void rechazarSolicitud(SolicitudEliminacion solicitud);
}
