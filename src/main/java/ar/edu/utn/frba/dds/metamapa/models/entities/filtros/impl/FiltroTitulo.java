package ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroString;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("TITULO")
public class FiltroTitulo extends FiltroString {
  public FiltroTitulo(String stringBuscado) {
    super(stringBuscado);
  }
}