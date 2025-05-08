package ar.edu.utn.frba.dds.metamapa.models.repositories.impl;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ColeccionesRepository implements IColeccionesRepository {
  private List<Coleccion> colecciones;

  public ColeccionesRepository() {
    this.colecciones = new ArrayList<>();
  }

  @Override
  public List<Coleccion> findAll() {
    return this.colecciones;
  }

  @Override
  public void save(Coleccion coleccion) {
    coleccion.setId((long) this.colecciones.size());
    this.colecciones.add(coleccion);
  }

  @Override
  public Coleccion findById(Long id) {
    return this.colecciones.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
  }
}
