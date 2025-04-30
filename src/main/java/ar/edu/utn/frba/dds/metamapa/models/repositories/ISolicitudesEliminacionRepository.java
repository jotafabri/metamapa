package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.SolicitudEliminacion;

public interface ISolicitudesEliminacionRepository {

  public List<SolicitudEliminacion> findAll();
  public void agregarSolicitud(SolicitudEliminacion solicitud);


}
