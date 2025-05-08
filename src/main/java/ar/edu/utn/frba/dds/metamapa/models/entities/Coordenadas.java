package ar.edu.utn.frba.dds.metamapa.models.entities;

import lombok.Getter;

@Getter
public class Coordenadas {
  private Float latitud;
  private Float longitud;

  public Coordenadas(Float latitud, Float longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

  public static Coordenadas fromString(String string) {
    if (string == null) {
      return null;
    }
    String[] partes = string.split(",");
    float lat = Float.parseFloat(partes[0].trim());
    float lon = Float.parseFloat(partes[1].trim());
    return new Coordenadas(lat, lon);
  }
}
