package ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroString;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AttributeOverride(name = "stringBuscado", column = @Column(name = "nombre_categoria"))
@DiscriminatorValue("categoria")
public class FiltroCategoria extends FiltroString {
  public FiltroCategoria(String stringBuscado) {
    super(stringBuscado);
  }
}