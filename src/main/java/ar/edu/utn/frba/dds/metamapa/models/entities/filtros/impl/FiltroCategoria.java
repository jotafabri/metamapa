package ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroString;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("CATEGORIA")
public class FiltroCategoria extends FiltroString {
  public FiltroCategoria(String stringBuscado) {
    super(stringBuscado);
  }
}