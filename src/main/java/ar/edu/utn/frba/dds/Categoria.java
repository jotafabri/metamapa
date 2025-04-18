package ar.edu.utn.frba.dds;

import java.util.List;

public class Categoria {
    private String nombre;
    private List<Hecho> hechos;




    public Categoria(String nombre) {
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }
    public List<Hecho> getHechos() {
        return hechos;
    }

    public void agregarHecho(Hecho hecho){
        hechos.add(hecho);
    }
}
