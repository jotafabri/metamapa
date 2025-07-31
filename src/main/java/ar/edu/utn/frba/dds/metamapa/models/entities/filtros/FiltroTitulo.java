package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;

public class FiltroTitulo implements Filtro {
  private final String tituloBuscado;

  public FiltroTitulo(String titulo) {
    this.tituloBuscado = titulo;
  }
  // Acá podría haber un comparador de String que sea más flexible

  @Override
  public boolean cumple(Hecho hecho) {
    return (hecho.getTitulo().equals(tituloBuscado));
  }
}