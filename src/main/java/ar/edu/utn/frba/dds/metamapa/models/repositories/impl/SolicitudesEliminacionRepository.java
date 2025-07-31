package ar.edu.utn.frba.dds.metamapa.models.repositories.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.utn.frba.dds.metamapa.models.entities.SolicitudEliminacion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.ISolicitudesEliminacionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SolicitudesEliminacionRepository implements ISolicitudesEliminacionRepository {

  private Map<Long, SolicitudEliminacion> solicitudesEliminacion = new HashMap<>();

  @Override
  public List<SolicitudEliminacion> findAll() {
    return new ArrayList<>(solicitudesEliminacion.values());
  }

  @Override
  public SolicitudEliminacion findById(Long id) {
    return this.solicitudesEliminacion.get(id);
  }

  @Override
  public void save(SolicitudEliminacion solicitud) {
    Long id;
    if (solicitud.getId() == null) {
      id = (long) this.solicitudesEliminacion.size();
      solicitud.setId(id);
    } else {
      id = solicitud.getId();
    }
    this.solicitudesEliminacion.put(id, solicitud);
  }
}
