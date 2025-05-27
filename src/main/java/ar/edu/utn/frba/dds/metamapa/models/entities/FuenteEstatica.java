package ar.edu.utn.frba.dds.metamapa.models.entities;


import java.util.ArrayList;
import java.util.List;

public class FuenteEstatica extends Fuente {
  protected List<Hecho> hechos = new ArrayList<>();

  public void importarHechos(String ruta) {
   this.hechos = new LectorCSV().leer(ruta);
  }

  public List<Hecho> getHechos() {
    return (this.hechos.stream().filter(h -> h.getEliminado().equals(false)).toList());
  }
}
