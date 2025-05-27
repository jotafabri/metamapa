package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;


@Getter
public class FuenteDinamica extends Fuente {
  protected List<Hecho> hechos = new ArrayList<>();

  public List<Hecho> getHechos() {
    return (hechos.stream().filter(h -> h.getEliminado().equals(false)).toList());
  }

  public Hecho buscarHechoPorTitulo(String titulo) {
    return getHechos().stream().filter(h -> titulo.equals(h.getTitulo())).findFirst().orElse(null);
  }

  public void etiquetarPorTitulo(String titulo, String etiqueta) {
    buscarHechoPorTitulo(titulo).agregarEtiqueta(etiqueta);
  }

  public void agregarHecho(Hecho hecho) {
    if (!this.hechos.contains(hecho)) {
      this.hechos.add(hecho);
    }
  }

  public void agregarHechos(List<Hecho> hechos) {
    hechos.forEach(this::agregarHecho);
  }
}