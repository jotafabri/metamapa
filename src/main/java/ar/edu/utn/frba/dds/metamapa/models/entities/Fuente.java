package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Fuente {
  protected List<Hecho> listaHechos = new ArrayList<>();

  public List<Hecho> getListaHechos() {
    return listaHechos;
  }

  public Hecho buscarHechoPorTitulo(String titulo) {
    //  filter + findAny
    return listaHechos.stream().filter(h -> titulo.equals(h.getTitulo())).findFirst().orElse(null);
  }

  public void etiquetarPorTitulo(String titulo, String etiqueta) {
    buscarHechoPorTitulo(titulo).agregarEtiqueta(etiqueta);
  }
}
