package ar.edu.utn.frba.dds.metamapa.models.entities;

public class CriterioUbicacion implements CriterioPertenencia {

  private Float latitud;
  private Float longitud;

  public CriterioUbicacion(Float latitud, Float longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

  public static CriterioUbicacion fromString(String string) {
    String[] partes = string.split(",");
    float lat = Float.parseFloat(partes[0].trim());
    float lon = Float.parseFloat(partes[1].trim());
    return new CriterioUbicacion(lat,lon);
  }

  @Override
  public boolean cumple(Hecho hecho) {
    return (hecho.getLatitud().equals(this.latitud))
        && (hecho.getLongitud().equals(this.longitud));
  }
}
