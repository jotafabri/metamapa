package ar.edu.utn.frba.dds.metamapa.models.entities;


public interface Filtro {
  boolean cumple(Hecho hecho);
}
