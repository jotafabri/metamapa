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

  public List<Hecho> importarHechos() {
    List<Hecho> listaHechos = new ArrayList<>();
    List<Hecho> hechosFuente = this.fuente.getListaHechos();
    for (Hecho hecho : hechosFuente) {
      boolean cumpleTodos = criterios.stream().allMatch(criterio -> criterio.cumple(hecho));
      if (cumpleTodos || criterios.isEmpty()) {
        listaHechos.add(hecho);
      }
    }
    return listaHechos;
  }

  public void navegar(List<CriterioPertenencia> criterios) {
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