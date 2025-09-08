package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Estadistica;

public interface IEstadisticasRepository {

  public List<Estadistica> findAll();

  public void save(Estadistica estadistica);

  public void deleteAll();
}