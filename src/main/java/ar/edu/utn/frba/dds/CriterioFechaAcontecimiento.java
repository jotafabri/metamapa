package ar.edu.utn.frba.dds;

import java.time.LocalDateTime;

public class CriterioFechaAcontecimiento implements CriterioPertenencia {

  private LocalDateTime desde;
  private LocalDateTime hasta;

  public CriterioFechaAcontecimiento(LocalDateTime desde, LocalDateTime hasta) {
    this.desde = desde;
    this.hasta = hasta;
  }

  @Override
  public boolean cumple(Hecho hecho) {
    LocalDateTime fecha = hecho.getFechaAcontecimiento();
    return (fecha.isEqual(desde) || fecha.isAfter(desde)) &&
        (fecha.isEqual(hasta) || fecha.isBefore(hasta));
  }
}
