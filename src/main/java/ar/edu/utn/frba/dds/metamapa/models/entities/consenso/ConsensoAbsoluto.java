package ar.edu.utn.frba.dds.metamapa.models.entities.consenso;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;

public class ConsensoAbsoluto implements EstrategiaConsenso {
  @Override
  public boolean cumple(Hecho hecho, List<Fuente> fuentes) {
    return fuentes.stream().allMatch(f -> f.getHechos().contains(hecho));
  }
}
