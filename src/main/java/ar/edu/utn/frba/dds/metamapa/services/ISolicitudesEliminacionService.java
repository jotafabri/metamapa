package ar.edu.utn.frba.dds.metamapa.services;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.SolicitudEliminacionDTO;

public interface ISolicitudesEliminacionService {

  void crearSolicitud(SolicitudEliminacionDTO solicitud);
}
