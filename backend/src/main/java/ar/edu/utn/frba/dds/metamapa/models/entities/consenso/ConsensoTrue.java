package ar.edu.utn.frba.dds.metamapa.models.entities.consenso;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;

public class ConsensoTrue implements EstrategiaConsenso {

  @Override
  public boolean cumple(Hecho hecho, List<Fuente> fuentes) {
    return true;
  }

  @Override
  public List<Hecho> filtrarConsensuados(List<Hecho> hechos, List<Fuente> fuentes) {
    return hechos;
  }

  @Override
  public String getNombre() {
    return "TRUE";
  }

}
