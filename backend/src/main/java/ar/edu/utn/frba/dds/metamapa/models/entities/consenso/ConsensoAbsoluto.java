package ar.edu.utn.frba.dds.metamapa.models.entities.consenso;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;

public class ConsensoAbsoluto implements EstrategiaConsenso {

  @Override
  public boolean cumple(Hecho hecho, List<Fuente> fuentes) {
    return fuentes.stream()
            .allMatch(f ->
                    f.getHechos().stream()
                            .anyMatch(h -> sonMismoHecho(h, hecho))
            );
  }

  @Override
  public List<Hecho> filtrarConsensuados(List<Hecho> hechos, List<Fuente> fuentes) {
    return hechos.stream()
            .filter(h -> cumple(h, fuentes))
            .toList();
  }

  private boolean sonMismoHecho(Hecho h1, Hecho h2) {
    if (h1.getTitulo() == null || h2.getTitulo() == null) return false;
    if (h1.getCategoria() == null || h2.getCategoria() == null) return false;

    return h1.getTitulo().equalsIgnoreCase(h2.getTitulo())
            && h1.getCategoria().equalsIgnoreCase(h2.getCategoria());
  }

  @Override
  public String getNombre() {
    return "MAYORIA_ABSOLUTA";
  }
}