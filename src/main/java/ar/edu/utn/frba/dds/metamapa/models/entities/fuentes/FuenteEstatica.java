package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;


import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.utils.LectorCSV;

public class FuenteEstatica extends Fuente {
  protected List<Hecho> hechos = new ArrayList<>();

  public void importarHechos(String ruta) {

    this.hechos = new LectorCSV().leer(ruta);
    for (Hecho hecho : this.hechos) {
      hecho.aceptar();
    }
  }

  public List<Hecho> getHechos() {
    return (this.hechos.stream().filter(h -> h.getEliminado().equals(false)).toList());
  }
}
