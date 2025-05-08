package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDateTime;

public class CriterioUbicacion implements CriterioPertenencia {

  private Float latitud;
  private Float longitud;

  public CriterioUbicacion(Coordenadas coordenadas) {
    this.latitud = coordenadas.getLatitud();
    this.longitud = coordenadas.getLongitud();
  }

  @Override
  public boolean cumple(Hecho hecho) {
    Coordenadas ubicacion = hecho.getCoordenadas();
    return (ubicacion.getLatitud().equals(this.latitud))
        && (ubicacion.getLongitud().equals(this.longitud));
  }
}
