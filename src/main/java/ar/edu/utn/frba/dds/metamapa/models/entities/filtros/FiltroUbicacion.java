package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;

public class FiltroUbicacion implements Filtro {

  private Float latitud;
  private Float longitud;

  public FiltroUbicacion(Float latitud, Float longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

  public static FiltroUbicacion fromString(String string) {
    String[] partes = string.split(",");
    float lat = Float.parseFloat(partes[0].trim());
    float lon = Float.parseFloat(partes[1].trim());
    return new FiltroUbicacion(lat, lon);
  }

  @Override
  public boolean cumple(Hecho hecho) {
    return (hecho.getLatitud().equals(this.latitud))
        && (hecho.getLongitud().equals(this.longitud));
  }
}
