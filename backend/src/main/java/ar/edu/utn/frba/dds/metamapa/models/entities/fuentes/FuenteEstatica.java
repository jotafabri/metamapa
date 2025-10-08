package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;


import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Origen;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.utils.LectorCSV;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@DiscriminatorValue("estatica")
public class FuenteEstatica extends Fuente {

  public FuenteEstatica(String ruta) {
    this.ruta = ruta;
  }

  @PostLoad
  @PostPersist
  private void cargarHechos() {
    if (hechos.isEmpty() && ruta != null) {
      this.hechos = new LectorCSV().leer(ruta);
      for (Hecho hecho : this.hechos) {
        hecho.aceptar();
        hecho.setFuente(this);
        hecho.setOrigen(Origen.DATASET);
      }
    }
  }

  public List<Hecho> getHechos() {
    return (this.hechos.stream().filter(h -> h.getEliminado().equals(false)).toList());
  }

  public Hecho getHechoFromId(Long id) {
    return this.hechos.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
  }
}
