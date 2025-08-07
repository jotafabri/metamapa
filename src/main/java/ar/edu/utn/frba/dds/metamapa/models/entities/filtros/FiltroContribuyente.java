package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;

public class FiltroContribuyente implements Filtro {
  private final boolean debeTenerContribuyente;

  public FiltroContribuyente(boolean debeTenerContribuyente) {
    this.debeTenerContribuyente = debeTenerContribuyente;
  }

  @Override
  public boolean cumple(Hecho hecho) {
    if (!debeTenerContribuyente) {
      return true;  // no filtrar por contribuyente
    }
    return hecho.getContribuyente() != null;
  }
}
