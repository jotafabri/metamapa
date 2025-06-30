package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;

public class FiltroCategoria implements Filtro {
    private final String categoriaBuscada;

    public FiltroCategoria(String categoriaBuscada) {
        this.categoriaBuscada = categoriaBuscada;
    }
    // Acá podría haber un comparador de String que sea más flexible

    @Override
    public boolean cumple(Hecho hecho) {
        return (hecho.getCategoria().equals(categoriaBuscada));
    }
}