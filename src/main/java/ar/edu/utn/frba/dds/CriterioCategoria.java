package ar.edu.utn.frba.dds;

public class CriterioCategoria implements CriterioPertenencia {
  private final String categoriaBuscada;

  public CriterioCategoria(String categoriaBuscada) {
    this.categoriaBuscada = categoriaBuscada;
  }
  // Acá podría haber un comparador de String que sea más flexible

  @Override
  public boolean cumple(Hecho hecho) {
    return (hecho.getCategoria().equals(categoriaBuscada));
  }
}
