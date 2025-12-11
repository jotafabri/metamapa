package ar.edu.utn.frba.dds.metamapa.models.entities.consenso;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;

public class ConsensoAbsoluto implements EstrategiaConsenso {
  @Override
  public boolean cumple(Hecho hecho, List<Fuente> fuentes) {
    return fuentes.stream().allMatch(f -> f.getHechos().contains(hecho)); // TODO revisar que el hecho sea parecido
  }

  @Override
  public List<Hecho> filtrarConsensuados(List<Hecho> hechos, List<Fuente> fuentes) {
    Set<String> titulosEnTodasFuentes = fuentes.stream()
        .map(f -> f.getHechos().stream()
            .map(h -> h.getTitulo().toLowerCase())
            .collect(Collectors.toSet()))
        .reduce((set1, set2) -> {
          set1.retainAll(set2);
          return set1;
        })
        .orElse(Set.of());

    return hechos.stream()
        .filter(h -> titulosEnTodasFuentes.contains(h.getTitulo().toLowerCase()))
        .toList();
  }

  @Override
  public String getNombre() {
    return "MAYORIA_ABSOLUTA";
  }
}
