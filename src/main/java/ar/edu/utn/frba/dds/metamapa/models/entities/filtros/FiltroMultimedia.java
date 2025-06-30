package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;

public class FiltroMultimedia implements Filtro {
    private final boolean debeTenerMultimedia;

    public FiltroMultimedia(boolean debeTenerMultimedia) {
        this.debeTenerMultimedia = debeTenerMultimedia;
    }

    @Override
    public boolean cumple(Hecho hecho) {
        if (!debeTenerMultimedia) {
            return true;
        }
        return hecho.getMultimedia() != null;
    }
}
