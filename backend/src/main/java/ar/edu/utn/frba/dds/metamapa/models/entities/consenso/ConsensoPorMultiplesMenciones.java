package ar.edu.utn.frba.dds.metamapa.models.entities.consenso;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;

public class ConsensoPorMultiplesMenciones implements EstrategiaConsenso {

    @Override
    public boolean cumple(Hecho hecho, List<Fuente> fuentes) {
        // Contamos cuántas FUENTES contienen un hecho igual al nuestro
        long fuentesConHechoIgual = fuentes.stream()
                .filter(f -> f.getHechos().stream().anyMatch(h -> sonIgualesFlexible(h, hecho)))
                .count();

        if (fuentesConHechoIgual < 2) {
            // Menos de 2 fuentes con el mismo hecho → no cumple
            return false;
        }

        // Verificamos que no haya CONFLICTO: ninguna otra fuente con el mismo título pero hecho distinto
        boolean existeConflicto = fuentes.stream()
                .flatMap(f -> f.getHechos().stream())
                .filter(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()))
                .anyMatch(h -> !sonIgualesFlexible(h, hecho));

        return !existeConflicto;
    }


    private boolean sonIgualesFlexible(Hecho h1, Hecho h2) {
        // título y categoría exactos (después de normalización)
        if (!h1.getTitulo().equalsIgnoreCase(h2.getTitulo())) return false;
        if (!h1.getCategoria().equalsIgnoreCase(h2.getCategoria())) return false;

        // tolerancia geográfica ~2 km
        double tolerancia = 0.018; // aprox 2 km
        if (Math.abs(h1.getLatitud() - h2.getLatitud()) > tolerancia) return false;
        if (Math.abs(h1.getLongitud() - h2.getLongitud()) > tolerancia) return false;

        // fecha exacta de acontecimiento
        return h1.getFechaAcontecimiento().equals(h2.getFechaAcontecimiento());
    }


  @Override
  public List<Hecho> filtrarConsensuados(List<Hecho> hechos, List<Fuente> fuentes) {
    Map<String, List<Hecho>> hectosPorTitulo = fuentes.stream()
        .flatMap(f -> f.getHechos().stream())
        .collect(Collectors.groupingBy(h -> h.getTitulo().toLowerCase()));

    Map<String, Integer> conteoFuentesPorTitulo = fuentes.stream()
        .flatMap(f -> f.getHechos().stream().map(h -> new AbstractMap.SimpleEntry<>(h.getTitulo().toLowerCase(), f)))
        .distinct()
        .collect(Collectors.groupingByConcurrent(
            Map.Entry::getKey,
            Collectors.summingInt(e -> 1)
        ));

    return hechos.stream()
        .filter(h -> {
          String tituloLower = h.getTitulo().toLowerCase();
          int countFuentes = conteoFuentesPorTitulo.getOrDefault(tituloLower, 0);

          if (countFuentes < 2) return false;

          List<Hecho> hechosSameTitulo = hectosPorTitulo.getOrDefault(tituloLower, List.of());
          return hechosSameTitulo.stream()
              .allMatch(other -> sonIgualesFlexible(h, other));
        })
        .toList();
  }
  @Override
  public String getNombre() {
    return "MULTIPLES_MENCIONES";
  }

}
