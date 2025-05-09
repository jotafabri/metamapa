package ar.edu.utn.frba.dds.metamapa.models.repositories.impl;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.SolicitudEliminacion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.ISolicitudesEliminacionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SolicitudesEliminacionRepository implements ISolicitudesEliminacionRepository {

  private List<SolicitudEliminacion> solicitudesEliminacion;

  public SolicitudesEliminacionRepository(List<SolicitudEliminacion> solicitudesEliminacion) {
    this.solicitudesEliminacion = solicitudesEliminacion;
  }

  @Override
  public List<SolicitudEliminacion> findAll() {
    return this.solicitudesEliminacion;
  }

  @Override
  public void agregarSolicitud(SolicitudEliminacion solicitud) {
    solicitud.setId((long) this.solicitudesEliminacion.size());
    this.solicitudesEliminacion.add(solicitud);
  }

}
