package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;

public class FiltroFechaCarga implements Filtro {

    private LocalDateTime desde;
    private LocalDateTime hasta;

    public FiltroFechaCarga(LocalDateTime desde, LocalDateTime hasta) {
        if (desde != null) {
            this.desde = desde;
        }
        if (hasta != null) {
            this.hasta = hasta;
        }
    }

    @Override
    public boolean cumple(Hecho hecho) {
        LocalDateTime fecha = hecho.getFechaCarga();
        return (fecha.isEqual(desde) || fecha.isAfter(desde)) &&
                (fecha.isEqual(hasta) || fecha.isBefore(hasta));
    }
}