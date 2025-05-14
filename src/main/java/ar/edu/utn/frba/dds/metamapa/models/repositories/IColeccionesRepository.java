package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;

public interface IColeccionesRepository {

  List<Coleccion> findAll();

  void save(Coleccion coleccion);

  Coleccion findByHandle(String handle);
}