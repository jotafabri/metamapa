package ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.Filtro;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Ubicacion;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("ubicacion")
public class FiltroUbicacion extends Filtro {

  @Column(name = "pais")
  private String pais;

  @Column(name = "provincia")
  private String provincia;

  @Column(name = "localidad")
  private String localidad;

  @Override
  public Boolean cumple(Hecho hecho) {
    Ubicacion ubicacion = hecho.getUbicacion();
    if (ubicacion == null) {
      return false;
    }
    Boolean hayPais = pais == null || pais.isEmpty() || ubicacion.getPais().equals(pais);
    Boolean hayProvincia = provincia == null || provincia.isEmpty() || ubicacion.getProvincia().equals(provincia);
    Boolean hayLocalidad = localidad == null || localidad.isEmpty() || ubicacion.getLocalidad().equals(localidad);
    return hayPais && hayProvincia && hayLocalidad;
  }
}
