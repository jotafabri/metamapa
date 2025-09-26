package ar.edu.utn.frba.dds.metamapa.models.entities.consenso;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;

public class ConsensoPorMultiplesMenciones implements EstrategiaConsenso {

  @Override
  public boolean cumple(Hecho hecho, List<Fuente> fuentes) {
    long coincidencias = fuentes.stream()
        .filter(f -> f.getHechos()
            .stream()
            .anyMatch(h -> h.getTitulo().equals(hecho.getTitulo())))
        .count();

    boolean existeConflicto = fuentes.stream()
        .flatMap(f -> f.getHechos().stream())
        .filter(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()))
        .anyMatch(h -> !sonIguales(hecho, h));

    return coincidencias >= 2 && !existeConflicto;
  }

  private boolean sonIguales(Hecho h1, Hecho h2) {
    if (!h1.getTitulo().equalsIgnoreCase(h2.getTitulo())) return false;
    if (!h1.getDescripcion().equalsIgnoreCase(h2.getDescripcion())) return false;
    if (!h1.getCategoria().equalsIgnoreCase(h2.getCategoria())) return false;
    if (Double.compare(h1.getLatitud(), h2.getLatitud()) != 0) return false;
    if (Double.compare(h1.getLongitud(), h2.getLongitud()) != 0) return false;
    return h1.getFechaAcontecimiento().equals(h2.getFechaAcontecimiento());
  }
}
