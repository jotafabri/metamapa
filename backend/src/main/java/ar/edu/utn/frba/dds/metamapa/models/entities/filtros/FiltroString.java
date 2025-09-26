package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class FiltroString extends Filtro {
  private String stringBuscado;

  @Override
  public Boolean cumple(Hecho hecho) {
    return (hecho.getCategoria().equals(stringBuscado));
  }
}
