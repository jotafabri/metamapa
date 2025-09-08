package ar.edu.utn.frba.dds.metamapa.models.entities.hechos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ubicacion {
  @Column(name = "localidad")
  private String localidad;

  @Column(name = "provincia")
  private String provincia;

  @Column(name = "pais")
  private String pais;
}
