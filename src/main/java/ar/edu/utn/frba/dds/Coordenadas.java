package ar.edu.utn.frba.dds;

import lombok.Getter;

@Getter
public class Coordenadas {
  private Float latitud;
  private Float longitud;

  public Coordenadas(Float latitud, Float longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }
}
