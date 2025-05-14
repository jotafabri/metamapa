package ar.edu.utn.frba.dds.metamapa.models.repositories.impl;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.SolicitudEliminacion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.ISolicitudesEliminacionRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class SolicitudesEliminacionRepository implements ISolicitudesEliminacionRepository {

  private List<SolicitudEliminacion> solicitudesEliminacion;

  public SolicitudesEliminacionRepository(List<SolicitudEliminacion> solicitudesEliminacion) {
    this.solicitudesEliminacion = solicitudesEliminacion;
  }

  public void save(SolicitudEliminacion solicitud){
    solicitudesEliminacion.add(solicitud);
  }
}

