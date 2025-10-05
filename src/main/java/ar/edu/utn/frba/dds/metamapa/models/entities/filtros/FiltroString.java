package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class FiltroString extends Filtro {

  @Column(name = "string_buscado")
  protected String stringBuscado;

  @Override
  public Boolean cumple(Hecho hecho) {
    return hecho.getCategoria() != null &&
            stringBuscado != null &&
            hecho.getCategoria().trim().equalsIgnoreCase(stringBuscado.trim());
  }

}
