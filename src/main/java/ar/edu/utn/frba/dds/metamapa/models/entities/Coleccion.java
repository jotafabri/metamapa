package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.ConsensoTrue;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.EstrategiaConsenso;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.Filtro;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Coleccion {
  private String handle;
  private String titulo;
  private String descripcion;
  private List<Fuente> fuentes = new ArrayList<>();
  private List<Filtro> criterios = new ArrayList<>();
  private List<Hecho> hechos = new ArrayList<>();
  private List<Hecho> hechosConsensuados = new ArrayList<>();
  private EstrategiaConsenso algoritmoDeConsenso = new ConsensoTrue();

  public Coleccion(String titulo, String descripcion) {
    this.titulo = titulo;
    this.descripcion = descripcion;
  }

  public void agregarFuente(Fuente fuente) {
    if (!this.fuentes.contains(fuente)) {
      this.fuentes.add(fuente);
    }
  }

  public void eliminarFuente(Fuente fuente) {
    this.fuentes.remove(fuente);
  }

  public void agregarCriterio(Filtro criterio) {
    this.criterios.add(criterio);
  }

  public void actualizarColeccion() {
    List<Hecho> hechosFiltrados = new ArrayList<>();
    for (Fuente fuente : fuentes) {
      List<Hecho> hechosFuente = fuente.getHechos();
      hechosFiltrados.addAll(hechosFuente.stream().filter(h -> this.criterios.stream().allMatch(c -> c.cumple(h))).toList());
    }
    this.hechos = hechosFiltrados;
  }

  public List<Hecho> navegar(List<Filtro> criterios, Boolean curado) {
    var lista = curado ? this.hechosConsensuados : this.hechos;
    if (criterios == null || criterios.isEmpty()) {
      return lista.stream()
          .filter(h -> !h.getEliminado())
          .toList();
    }
    return lista.stream()
        .filter(h -> !h.getEliminado() && criterios.stream().allMatch(c -> c.cumple(h)))
        .toList();
  }

  // TODO hacer una estrategia de consenso que siempre devuelva true
  public void actualizarCurados() {
    this.hechosConsensuados = this.hechos.stream()
        .filter(h -> this.algoritmoDeConsenso.cumple(h, this.fuentes))
        .toList();
  }
}
