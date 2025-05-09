package ar.edu.utn.frba.dds.metamapa.services;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.SolicitudEliminacionDTO;

public interface ISolicitudesEliminacionService {

  public void crearSolicitud(SolicitudEliminacionDTO solicitud);
}
