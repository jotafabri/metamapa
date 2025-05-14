package ar.edu.utn.frba.dds.metamapa.models.repositories.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    boolean i = true;
    for (Coleccion c : colecciones) {
      if (Objects.equals(c.getHandle(), coleccion.getHandle())) {
        i = false;
        c.actualizarColeccion(coleccion);
      }
    }
    if (i) {
      colecciones.add(coleccion);
    }
  }

  @Override
  public Coleccion findByHandle(String handle) {
    return this.colecciones.stream().filter(c -> c.getHandle().equals(handle)).findFirst().orElse(null);
  }
}
