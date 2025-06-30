package ar.edu.utn.frba.dds.metamapa.models.repositories.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.EstadoHecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HechosRepository implements IHechosRepository {

  private List<Hecho> hechos = new ArrayList<Hecho>();


  @Override
  public Hecho findById(Long id) {
    return this.hechos
        .stream()
        .filter(s -> s.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  @Override
  public List<Hecho> findAll() {
    return new ArrayList<>(hechos); // Cambio
  }

  @Override
  public void save(Hecho hecho) {
    if (hecho.getId() == null) {
      hecho.setId((long) (this.hechos.size() + 1));
      this.hechos.add(hecho);
    } else {
      Hecho existente = findById(hecho.getId());
      if (existente == null) {
        throw new IllegalArgumentException("El hecho con ID " + hecho.getId() + " no existe.");
      } //Aqui estoy queriendo modificar un hecho con id inexistente.
      if (existente.getEstado() != Estado.ACEPTADA) {
        throw new IllegalStateException("Solo se pueden actualizar hechos en revisión.");
      } //Los hechos aseptados y rechazados no se pueden actualizar.
      update(hecho);
    }
  }


  @Override
  public void delete(Hecho hecho) {
    this.hechos.remove(hecho);
  }


  private void update(Hecho hechoActualizado) {
    for (int i = 0; i < hechos.size(); i++) {
      if (hechos.get(i).getId().equals(hechoActualizado.getId())) {
        hechos.set(i, hechoActualizado);
        return;
      }
    }
  }


}
