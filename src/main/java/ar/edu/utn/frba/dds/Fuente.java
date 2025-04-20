package ar.edu.utn.frba.dds;
import java.util.ArrayList;
import java.util.List;

public abstract class Fuente {
    protected List<Hecho> listaHechos = new ArrayList<>();

    public List<Hecho> getListaHechos() {
        return listaHechos;
    }
}
