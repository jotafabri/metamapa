package ar.edu.utn.frba.dds.metamapa.models.entities;

public interface CriterioPertenencia {
  boolean cumple(Hecho hecho);
}
