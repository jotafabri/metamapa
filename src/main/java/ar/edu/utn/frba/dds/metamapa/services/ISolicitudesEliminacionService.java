package ar.edu.utn.frba.dds.metamapa.services;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.SolicitudEliminacionDTO;

public interface ISolicitudesEliminacionService {

  void crearSolicitud(SolicitudEliminacionDTO solicitud);
}
