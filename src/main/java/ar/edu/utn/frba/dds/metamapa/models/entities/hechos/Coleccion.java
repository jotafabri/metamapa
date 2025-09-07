package ar.edu.utn.frba.dds.metamapa.models.entities.hechos;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.converters.EstrategiaConsensoAttributeConverter;
import ar.edu.utn.frba.dds.metamapa.models.entities.Persistente;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.ConsensoTrue;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.EstrategiaConsenso;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.Filtro;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "coleccion")
public class Coleccion extends Persistente {
  @Column(name = "handle")
  private String handle;

  @Column(name = "titulo")
  private String titulo;

  @Column(name = "descripcion")
  private String descripcion;

  @ManyToMany
  @JoinTable(
      name = "coleccion_fuente",
      joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "fuente_id", referencedColumnName = "id")
  )
  private List<Fuente> fuentes = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "coleccion_criterio",
      joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "criterio_id", referencedColumnName = "id")
  )
  private List<Filtro> criterios = new ArrayList<>();

  @Transient
  private List<Hecho> hechos = new ArrayList<>();

  @Transient
  private List<Hecho> hechosConsensuados = new ArrayList<>();

  @Convert(converter = EstrategiaConsensoAttributeConverter.class)
  @Column(name = "estrategia_consenso")
  private EstrategiaConsenso algoritmoDeConsenso;

  public Coleccion(String titulo, String descripcion) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.algoritmoDeConsenso = new ConsensoTrue();
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

  public void actualizarCurados() {
    this.hechosConsensuados = this.hechos.stream()
        .filter(h -> this.algoritmoDeConsenso.cumple(h, this.fuentes))
        .toList();
  }
}
