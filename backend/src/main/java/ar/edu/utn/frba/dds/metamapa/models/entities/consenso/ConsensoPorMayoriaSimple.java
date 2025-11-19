package ar.edu.utn.frba.dds.metamapa.models.entities.consenso;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;

public class ConsensoPorMayoriaSimple implements EstrategiaConsenso {
  @Override
  public boolean cumple(Hecho hecho, List<Fuente> fuentes) {
    long cantidad = fuentes.stream()
        .filter(f -> f.getHechos()
            .stream()
            .anyMatch(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo())))
        .count();
    return cantidad >= (Math.ceil(fuentes.size() / 2.0));
  }

  @Override
  public String getNombre() {
    return "MAYORIA_SIMPLE";
  }

}
