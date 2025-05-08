package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;

public interface IHechosRepository {
  public List<Hecho> findAll();

  public void save(Hecho hecho);
}
