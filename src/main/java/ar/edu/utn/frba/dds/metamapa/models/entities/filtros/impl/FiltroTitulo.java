package ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroString;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "criterio_titulo")
@AttributeOverride(name = "stringBuscado", column = @Column(name = "titulo"))
public class FiltroTitulo extends FiltroString {
  public FiltroTitulo(String stringBuscado) {
    super(stringBuscado);
  }
}