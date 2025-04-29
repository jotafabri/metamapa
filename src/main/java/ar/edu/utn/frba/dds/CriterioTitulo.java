package ar.edu.utn.frba.dds;

public class CriterioTitulo implements CriterioPertenencia {
  private final String tituloBuscado;

  public CriterioTitulo(String titulo) {
    this.tituloBuscado = titulo;
  }
  // Acá podría haber un comparador de String que sea más flexible

  @Override
  public boolean cumple(Hecho hecho) {
    return (hecho.getTitulo().equals(tituloBuscado));
  }
}
