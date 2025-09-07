package ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl;

import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroFecha;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "criterio_fechaCarga")
public class FiltroFechaCarga extends FiltroFecha {

  public FiltroFechaCarga(LocalDateTime desde, LocalDateTime hasta) {
    super(desde, hasta);
  }

  @Override
  protected LocalDateTime getFecha(Hecho hecho) {
    return hecho.getFechaCarga();
  }
}