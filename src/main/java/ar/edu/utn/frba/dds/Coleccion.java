package ar.edu.utn.frba.dds;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Coleccion {
  private String titulo;
  private String descripcion;
  private Fuente fuente;
  public List<CriterioPertenencia> criterios;

  public Coleccion(String titulo, String descripcion, Fuente fuente, List<CriterioPertenencia> criterios) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.fuente = fuente;
    if(null == criterios){ this.criterios= new ArrayList();}
    else{this.criterios = criterios;}
  }

  public void agregarCriterio(CriterioPertenencia criterio){
    this.criterios.add(criterio);
  }

  // TODO darHechos seria un nombre mas apropiado
  public List<Hecho> importarHechos() {
    List<Hecho> listaHechos = new ArrayList<>();
    List<Hecho> hechosFuente = this.fuente.getListaHechos();
    for (Hecho hecho : hechosFuente) {
      //pasar a filter
      boolean cumpleTodos = criterios.stream().allMatch(criterio -> criterio.cumple(hecho));
      if (cumpleTodos || criterios.isEmpty()) {
        listaHechos.add(hecho);
      }
    }
    return listaHechos;
  }

  // TODO Esto tendria que devolver una lista de hechos, sin printlns
  public void navegar(List<CriterioPertenencia> criterios /* TODO asumir que viene como lista vacia para no usar null*/) {
    for (Hecho hecho : this.importarHechos()) {
      if(criterios != null){
        boolean cumpleTodos = criterios.stream().allMatch(criterio -> criterio.cumple(hecho));
        if (cumpleTodos) {
          System.out.println(hecho.printHecho());
          System.out.println("-----------------------------");
        }
      }
      else{
        System.out.println(hecho.printHecho());
        System.out.println("-----------------------------");
      }
    }
  }

//  Como persona visualizadora, deseo navegar todos los hechos disponibles de una colección.
//  Como persona visualizadora, deseo navegar los hechos disponibles de una colección, aplicando filtros.
}