package ar.edu.utn.frba.dds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Coleccion {
  private String titulo;
  private String descripcion;
  private Fuente fuente;
  private List<Hecho> listaHechos;
  public List<CriterioPertenencia> criterios;

  public Coleccion(String titulo, String descripcion, Fuente fuente, List<CriterioPertenencia> criterios) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.fuente = fuente;
    this.criterios = criterios;
  }

  public void ImportarHechos() {
    List<Hecho> hechosFuente = fuente.getListaHechos();
    for (Hecho hecho : hechosFuente) {
      boolean cumpleTodos = criterios.stream().allMatch(criterio -> criterio.cumple(hecho));
      if (cumpleTodos) {
        listaHechos.add(hecho);
      }
    }
  }
}
