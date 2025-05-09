package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;

public interface IHechosRepository {
  List<Hecho> findAll();

  void save(Hecho hecho);

  Hecho findById(Long id);
}
