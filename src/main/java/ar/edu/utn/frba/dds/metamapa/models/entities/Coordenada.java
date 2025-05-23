package ar.edu.utn.frba.dds.metamapa.models.entities;

import lombok.Getter;

@Getter
public class Coordenada {
  private Float latitud;
  private Float longitud;

  public Coordenada(Float latitud, Float longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

  public static Coordenada fromString(String string) {
    if (string == null) {
      return null;
    }
    String[] partes = string.split(",");
    float lat = Float.parseFloat(partes[0].trim());
    float lon = Float.parseFloat(partes[1].trim());
    return new Coordenada(lat, lon);
  }
}
