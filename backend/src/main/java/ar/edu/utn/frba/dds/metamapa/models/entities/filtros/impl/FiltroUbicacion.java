package ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.Filtro;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "criterio_ubicacion")
public class FiltroUbicacion extends Filtro {

  @Column(name = "latitud")
  private Double latitud;

  @Column(name = "longitud")
  private Double longitud;

  public static FiltroUbicacion fromString(String string) {
    String[] partes = string.split(",");
    Double lat = Double.parseDouble(partes[0].trim());
    Double lon = Double.parseDouble(partes[1].trim());
    return new FiltroUbicacion(lat, lon);
  }

  @Override
  public Boolean cumple(Hecho hecho) {
    return (hecho.getLatitud().equals(this.latitud))
        && (hecho.getLongitud().equals(this.longitud));
  }
}
