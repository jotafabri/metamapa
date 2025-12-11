package ar.edu.utn.frba.dds.metamapa.models.entities.consenso;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

public List<Hecho> filtrarConsensuados(List<Hecho> hechos, List<Fuente> fuentes) {
    int umbralMinimo = (int) Math.ceil(fuentes.size() / 2.0);

    Map<String, Integer> conteoTitulos = fuentes.stream()
        .flatMap(f -> f.getHechos().stream())
        .collect(Collectors.groupingByConcurrent(
            h -> h.getTitulo().toLowerCase(),
            Collectors.summingInt(h -> 1)
        ));

    return hechos.stream()
        .filter(h -> conteoTitulos.getOrDefault(h.getTitulo().toLowerCase(), 0) >= umbralMinimo)
        .toList();
  }

  @Override
  public String getNombre() {
    return "MAYORIA_SIMPLE";
  }

}
