package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;

public class FiltroFechaAcontecimiento implements Filtro {

  private LocalDateTime desde;
  private LocalDateTime hasta;

  public FiltroFechaAcontecimiento(LocalDateTime desde, LocalDateTime hasta) {
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