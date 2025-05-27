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
  private List<Fuente> fuentes = new ArrayList<Fuente>();
  private List<CriterioPertenencia> criterios = new ArrayList<CriterioPertenencia>();
  private List<Hecho> listaHechos = new ArrayList<>();


  public Coleccion(String titulo, String descripcion, List<CriterioPertenencia> criterios) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    if (null == criterios) {
      this.criterios = new ArrayList();
    } else {
      this.criterios = criterios;
    }
  }

  public void agregarFuente(Fuente fuente) {
    if(!fuentes.contains(fuente)) this.fuentes.add(fuente);
  }

  public void agregarCriterio(CriterioPertenencia criterio) {
    this.criterios.add(criterio);
  }

  public void actualizarColeccion() {
    List<Hecho> hechosFiltrados = new ArrayList<>();
    for (Fuente fuente1 : fuentes) {
      List<Hecho> hechosFuente = fuente1.getListaHechos();
      hechosFiltrados.addAll(hechosFuente.stream().filter(h -> this.criterios.stream().allMatch(c -> c.cumple(h))).toList());
    }
  }

  public List<Hecho> darHechos() {
    return this.getListaHechos();
  }

  public List<Hecho> navegar(List<CriterioPertenencia> criterios) {
    var listaHechos = this.darHechos();
    var listaFiltrada = listaHechos.stream().filter(h -> !h.getEliminado() && criterios.stream().allMatch(c -> c.cumple(h))).toList();
    return listaFiltrada;
  }
}
