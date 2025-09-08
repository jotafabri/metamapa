package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class FiltroBoolean extends Filtro {
  private Boolean condicion;

  @Override
  public Boolean cumple(Hecho hecho) {
    if (!condicion) {
      return true;
    }
    return cumpleExtra(hecho);
  }

  protected abstract Boolean cumpleExtra(Hecho hecho);
}
