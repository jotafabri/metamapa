package ar.edu.utn.frba.dds;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
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
    this.listaHechos = new ArrayList<>();
  }

  public void importarHechos() {
    List<Hecho> hechosFuente = this.fuente.getListaHechos();
    for (Hecho hecho : hechosFuente) {
      boolean cumpleTodos = criterios.stream().allMatch(criterio -> criterio.cumple(hecho));
      if (cumpleTodos) {
        listaHechos.add(hecho);
      }
    }
  }

  public void navegar(Hecho filtro) {
    List<Hecho> listaFiltrada = this.listaHechos.stream()
//        .filter(h -> filtro.getTitulo() == null | h.getTitulo().equals(filtro.getTitulo()))
//        .filter(h -> filtro.getDescripcion() == null | h.getDescripcion().equals(filtro.getDescripcion()))
//        .filter(h -> filtro.getCategoria() == null | h.getCategoria().equals(filtro.getCategoria()))
//        .filter(h -> filtro.getCoordenadas() == null | h.getCoordenadas().equals(filtro.getCoordenadas()))
//        .filter(h -> filtro.getFechaAcontecimiento() == null | h.getFechaAcontecimiento().equals(filtro.getFechaAcontecimiento()))
        .toList();
    for (Hecho hecho : listaFiltrada) {
      System.out.println(hecho.printHecho());
      System.out.println("-----------------------------");
    }
  }

//  Como persona visualizadora, deseo navegar todos los hechos disponibles de una colección.
//  Como persona visualizadora, deseo navegar los hechos disponibles de una colección, aplicando filtros.
}