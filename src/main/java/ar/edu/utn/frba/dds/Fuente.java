package ar.edu.utn.frba.dds;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Fuente {
    protected List<Hecho> listaHechos = new ArrayList<>();

    public List<Hecho> getListaHechos() {
        return listaHechos;
    }
    public Hecho buscarHechoPorTitulo(String titulo){
        for(Hecho hecho: listaHechos){
            if(Objects.equals(titulo, hecho.getTitulo())){
                return hecho;
            }
        }
        return null;
    }

    public void etiquetarPorTitulo(String titulo, String etiqueta){
        for(Hecho hecho: listaHechos){
            if(Objects.equals(titulo, hecho.getTitulo())){
                hecho.agregarEtiqueta(etiqueta);
            }
        }
    }
    public void listarHechos(){
        for (Hecho hecho : listaHechos){
            System.out.println(hecho.getTitulo());
        }
    }
}
