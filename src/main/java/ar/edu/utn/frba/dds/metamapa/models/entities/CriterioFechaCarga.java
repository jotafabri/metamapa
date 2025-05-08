package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDateTime;

public class CriterioFechaCarga implements CriterioPertenencia {

  private LocalDateTime desde;
  private LocalDateTime hasta;

  public CriterioFechaCarga(LocalDateTime desde, LocalDateTime hasta) {
    this.desde = desde;
    this.hasta = hasta;
  }

  @Override
  public boolean cumple(Hecho hecho) {
    LocalDateTime fecha = hecho.getFechaCarga();
    return (fecha.isEqual(desde) || fecha.isAfter(desde)) &&
        (fecha.isEqual(hasta) || fecha.isBefore(hasta));
  }
}
