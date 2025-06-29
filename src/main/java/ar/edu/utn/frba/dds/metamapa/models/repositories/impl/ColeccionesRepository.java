package ar.edu.utn.frba.dds.metamapa.models.repositories.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ColeccionesRepository implements IColeccionesRepository {
  //  private List<Coleccion> colecciones;
  private Map<String, Coleccion> colecciones = new HashMap<>();

  @Override
  public List<Coleccion> findAll() {
    return new ArrayList<>(colecciones.values());
  }

  @Override
  public void save(Coleccion coleccion) {
    if (coleccion.getHandle() == null) {
      String handle = generarHandleUnico(coleccion.getTitulo());
      coleccion.setHandle(handle);
      colecciones.put(handle, coleccion);
    } else {
      colecciones.put(coleccion.getHandle(), coleccion);
    }
  }

  @Override
  public void delete(String handle) {
    this.colecciones.remove(handle);
  }

  @Override
  public Coleccion findByHandle(String handle) {
    return this.colecciones.get(handle);
  }

  private String generarHandleUnico(String baseTitulo) {
    String base = baseTitulo.toLowerCase().replaceAll("[^a-z0-9]", "");
    String candidato = base;
    for (int i = 1; this.colecciones.containsKey(candidato); i++) {
      candidato = base + i;
    }
    return candidato;
  }
}
