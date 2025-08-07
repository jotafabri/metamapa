package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;


import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;

public interface Filtro {
  boolean cumple(Hecho hecho);
}
