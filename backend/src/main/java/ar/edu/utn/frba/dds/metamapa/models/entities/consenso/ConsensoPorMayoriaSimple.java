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
                .filter(f -> f.getHechos().stream().anyMatch(h ->
                        h.getTitulo().equalsIgnoreCase(hecho.getTitulo()) &&
                                h.getCategoria().equalsIgnoreCase(hecho.getCategoria())
                ))
                .count();

        return cantidad >= Math.ceil(fuentes.size() / 2.0);
    }

    @Override
    public List<Hecho> filtrarConsensuados(List<Hecho> hechos, List<Fuente> fuentes) {
        int umbralMinimo = (int) Math.ceil(fuentes.size() / 2.0);

        Map<String, Integer> conteo = fuentes.stream()
                .flatMap(f -> f.getHechos().stream())
                .collect(Collectors.groupingByConcurrent(
                        h -> clave(h),
                        Collectors.summingInt(h -> 1)
                ));

        return hechos.stream()
                .filter(h -> conteo.getOrDefault(clave(h), 0) >= umbralMinimo)
                .toList();
    }

    private String clave(Hecho h) {
        return (h.getTitulo() + "|" + h.getCategoria()).toLowerCase();
    }

    @Override
    public String getNombre() {
        return "MAYORIA_SIMPLE";
    }
}

