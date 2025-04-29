package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coleccion {
  private String titulo;
  private String descripcion;
  private Fuente fuente;
  public List<CriterioPertenencia> criterios;

  public Coleccion(String titulo, String descripcion, Fuente fuente, List<CriterioPertenencia> criterios) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.fuente = fuente;
    if (null == criterios) {
      this.criterios = new ArrayList();
    } else {
      this.criterios = criterios;
    }
  }

  public void agregarCriterio(CriterioPertenencia criterio) {
    this.criterios.add(criterio);
  }

  public List<Hecho> darHechos() {
    List<Hecho> hechosFuente = this.fuente.getListaHechos();
    List<Hecho> hechosFiltrados;
    hechosFiltrados = hechosFuente.stream().filter(h -> this.criterios.stream().allMatch(c -> c.cumple(h))).toList();
    return hechosFiltrados;
  }

  public List<Hecho> navegar(List<CriterioPertenencia> criterios) {
    var listaHechos = this.darHechos();
    var listaFiltrada = listaHechos.stream().filter(h -> criterios.stream().allMatch(c -> c.cumple(h))).toList();
    return listaFiltrada;
  }

//  Como persona visualizadora, deseo navegar todos los hechos disponibles de una colección.
//  Como persona visualizadora, deseo navegar los hechos disponibles de una colección, aplicando filtros.
}
