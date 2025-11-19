package ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroBoolean;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AttributeOverride(name = "condicion", column = @Column(name = "debeTenerMultimedia"))
@DiscriminatorValue("debe_tener_multimedia")
public class FiltroMultimedia extends FiltroBoolean {

  public FiltroMultimedia(Boolean condicion) {
    super(condicion);
  }

  @Override
  protected Boolean cumpleExtra(Hecho hecho) {
    return !hecho.getMultimedia().isEmpty();
  }
}
