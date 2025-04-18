package ar.edu.utn.frba.dds;

public class CriterioCategoria implements CriterioPertenencia {
    private final Categoria categoriaBuscada;

    public CriterioCategoria(Categoria categoriaBuscada) {
        this.categoriaBuscada = categoriaBuscada;
    }

    @Override
    public boolean cumple(Hecho hecho) {
        return (hecho.getCategoria().equals(categoriaBuscada));
    }
}

