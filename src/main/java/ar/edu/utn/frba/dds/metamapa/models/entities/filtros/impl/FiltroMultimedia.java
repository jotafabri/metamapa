package ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroBoolean;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("MULTIMEDIA")
public class FiltroMultimedia extends FiltroBoolean {

  public FiltroMultimedia(Boolean condicion) {
    super(condicion);
  }

  @Override
  protected Boolean cumpleExtra(Hecho hecho) {
    return !hecho.getMultimedia().isEmpty();
  }
}
