package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;


import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.utils.LectorCSV;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "fuente_estatica")
public class FuenteEstatica extends Fuente {
  @Column(name = "rutaCSV", nullable = false)
  private String ruta;

  public FuenteEstatica(String ruta) {
    this.ruta = ruta;
    this.hechos = new LectorCSV().leer(ruta);
    for (Hecho hecho : this.hechos) {
      hecho.aceptar();
    }
  }

  public List<Hecho> getHechos() {
    return (this.hechos.stream().filter(h -> h.getEliminado().equals(false)).toList());
  }

  public Hecho getHechoFromId(Long id) {
    return this.hechos.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
  }
}
