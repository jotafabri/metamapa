package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;

public interface IColeccionesRepository {

  public List<Coleccion> findAll();

  public void save(Coleccion coleccion);

  public Coleccion findById(Long id);
}