package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Coleccion {
  private String handle;
  private String titulo;
  private String descripcion;
  private Fuente fuente;
  private List<CriterioPertenencia> criterios = new ArrayList<>();

  public Coleccion(String titulo, String descripcion, String handle, Fuente fuente, List<CriterioPertenencia> criterios) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.handle = handle;
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

  public void actualizarColeccion(Coleccion coleccion) {
    //lógica que implique actualizar la colección
    this.titulo = coleccion.getTitulo();
    this.descripcion = coleccion.getDescripcion();
  }

  //puede ser por parámetro la lista o puede estar en un atributo, depende de lo que nos respondan por discord
  public List<Hecho> darHechos(List<Fuente> fuentes) {
    List<Hecho> hechosFiltrados= new ArrayList<>();
    for(Fuente fuente1 : fuentes){
      List<Hecho> hechosFuente = fuente1.getListaHechos();
      hechosFiltrados.addAll(hechosFuente.stream().filter(h -> this.criterios.stream().allMatch(c -> c.cumple(h))).toList());
    }
    return hechosFiltrados;
  }

  /*public List<Hecho> navegar(List<CriterioPertenencia> criterios) {
    var listaHechos = this.darHechos();
    var listaFiltrada = listaHechos.stream().filter(h -> criterios.stream().allMatch(c -> c.cumple(h))).toList();
    return listaFiltrada;
  }*/
}
