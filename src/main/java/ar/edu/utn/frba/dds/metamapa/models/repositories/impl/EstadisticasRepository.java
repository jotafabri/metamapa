package ar.edu.utn.frba.dds.metamapa.models.repositories.impl;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Estadistica;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IEstadisticasRepository;
import org.springframework.stereotype.Repository;

@Repository
public class EstadisticasRepository implements IEstadisticasRepository {

  private List<Estadistica> estadisticas = new ArrayList<Estadistica>();

  @Override
  public List<Estadistica> findAll() {
    return new ArrayList<>(estadisticas);
  }

  @Override
  public void save(Estadistica estadistica) {
    estadistica.setId((long) (this.estadisticas.size() + 1));
    this.estadisticas.add(estadistica);
  }

  @Override
  public void deleteAll() {
    this.estadisticas.clear();
  }
}