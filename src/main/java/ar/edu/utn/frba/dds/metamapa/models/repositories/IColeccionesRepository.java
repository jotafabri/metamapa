package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;

public interface IColeccionesRepository {

  List<Coleccion> findAll();

  void save(Coleccion coleccion);

  void delete(String handle);

  Coleccion findByHandle(String handle);
}