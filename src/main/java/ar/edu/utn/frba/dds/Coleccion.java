package ar.edu.utn.frba.dds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Coleccion {
    private String titulo;
    private String descripcion;
    private Fuente fuente;
    private List<Hecho> listaHechos;
//    public Criterio criterio;

    public Coleccion(String titulo, String descripcion, Fuente fuente/*, Criterio criterio*/) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuente = fuente;
        this.listaHechos = fuente.getListaHechos();

    }

}
