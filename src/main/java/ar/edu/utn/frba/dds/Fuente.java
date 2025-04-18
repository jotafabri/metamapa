package ar.edu.utn.frba.dds;

import java.io.File;
import java.util.List;

public class Fuente {
    private String ruta;
    private List<Hecho> listaHechos;

    public Fuente(String ruta){
        this.ruta = ruta;
    }

    public void importarHechos(LectorCSV lector){
        this.listaHechos = lector.obtenerHechos(ruta);
    }

    public List<Hecho> getListaHechos() {
        return listaHechos;
    }
}
