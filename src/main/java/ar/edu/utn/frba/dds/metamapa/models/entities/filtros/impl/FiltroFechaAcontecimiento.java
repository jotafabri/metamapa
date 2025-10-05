package ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl;

import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroFecha;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("FECHA_ACONTECIMIENTO")
public class FiltroFechaAcontecimiento extends FiltroFecha {

  public FiltroFechaAcontecimiento(LocalDateTime desde, LocalDateTime hasta) {
    super(desde, hasta);
  }

  @Override
  protected LocalDateTime getFecha(Hecho hecho) {
    return hecho.getFechaAcontecimiento();
  }
}