package ar.edu.utn.frba.dds.metamapa.models.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Fuente {
  protected List<Hecho> listaHechos = new ArrayList<>();
  @Getter
  @Setter
  private Long id= null;

  //TODO cambiar a getHechos debería ser suficiente
  //TODO podríamos pasar lista hechos para las subclases para evitar que proxy la tenga innecesariamente
  public List<Hecho> getListaHechos() {
    return listaHechos;
  }

  public Hecho buscarHechoPorTitulo(String titulo) {
    return getListaHechos().stream().filter(h -> titulo.equals(h.getTitulo())).findFirst().orElse(null);
  }

  //TODO esto en proxy no tiene sentido
  public void agregarHecho(Hecho hecho) {
    Optional<Hecho> hecho2 = getListaHechos().stream().filter(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo())).findFirst();
    hecho2.ifPresentOrElse(h ->
      h.actualizarHecho(hecho), () -> getListaHechos().add(hecho));
  }

  public void etiquetarPorTitulo(String titulo, String etiqueta) {
    buscarHechoPorTitulo(titulo).agregarEtiqueta(etiqueta);
  }
}
