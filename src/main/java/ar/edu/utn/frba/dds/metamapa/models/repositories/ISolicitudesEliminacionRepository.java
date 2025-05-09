package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.SolicitudEliminacion;

public interface ISolicitudesEliminacionRepository {

  List<SolicitudEliminacion> findAll();

  void agregarSolicitud(SolicitudEliminacion solicitud);
}
