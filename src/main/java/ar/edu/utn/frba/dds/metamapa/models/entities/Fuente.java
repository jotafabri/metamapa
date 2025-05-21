package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Fuente {
  protected List<Hecho> listaHechos = new ArrayList<>();

  public List<Hecho> getListaHechos() {
    return listaHechos;
  }

  public Hecho buscarHechoPorTitulo(String titulo) {
    return getListaHechos().stream().filter(h -> titulo.equals(h.getTitulo())).findFirst().orElse(null);
  }

  public void agregarHecho(Hecho hecho) {
    Optional<Hecho> hecho2 = getListaHechos().stream().filter(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo())).findFirst();
    hecho2.ifPresentOrElse(h ->
      h.actualizarHecho(hecho), () -> getListaHechos().add(hecho));
  }

  public void etiquetarPorTitulo(String titulo, String etiqueta) {
    buscarHechoPorTitulo(titulo).agregarEtiqueta(etiqueta);
  }
}
