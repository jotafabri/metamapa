package ar.edu.utn.frba.dds.metamapa.models.entities.hechos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.metamapa.converters.EstrategiaConsensoAttributeConverter;
import ar.edu.utn.frba.dds.metamapa.models.entities.Persistente;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.ConsensoTrue;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.EstrategiaConsenso;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.Filtro;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
  @Column(name = "handle", unique = true)
  private String handle;

  @Column(name = "titulo")
  private String titulo;

  @Column(name = "descripcion", columnDefinition = "TEXT")
  private String descripcion;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "coleccion_fuente", joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "fuente_id", referencedColumnName = "id"))
  private List<Fuente> fuentes = new ArrayList<>();

  @OneToMany(mappedBy = "coleccion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Filtro> criterios = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  @JoinTable(name = "coleccion_hecho", joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"))
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
    boolean sinCriterios = this.criterios.isEmpty();

    Set<Hecho> nuevosHechos = fuentes.stream()
        .flatMap(fuente -> fuente.getHechos().stream())
        .filter(h -> sinCriterios || this.criterios.stream().allMatch(c -> c.cumple(h)))
        .collect(Collectors.toSet());

    Set<Hecho> actuales = new HashSet<>(this.hechos);

    if (!actuales.equals(nuevosHechos)) {
      this.hechos.clear();
      this.hechos.addAll(nuevosHechos);
    }
  }

  public List<Hecho> navegar(List<Filtro> criterios, Boolean curado) {
    var lista = curado ? this.hechosConsensuados : this.hechos;
    return lista.stream()
        .filter(h -> criterios.isEmpty() || criterios.stream().allMatch(c -> c.cumple(h)))
        .toList();
  }

  public void actualizarCurados() {
    this.hechosConsensuados = this.algoritmoDeConsenso.filtrarConsensuados(this.hechos, this.fuentes);
    // Set<Hecho> nuevosConsensuados = this.hechos.stream()
    // .filter(h -> this.algoritmoDeConsenso.cumple(h, this.fuentes))
    // .collect(Collectors.toSet());
    //
    // Set<Hecho> actuales = new HashSet<>(this.hechosConsensuados);
    //
    // if (!actuales.equals(nuevosConsensuados)) {
    // this.hechosConsensuados.clear();
    // this.hechosConsensuados.addAll(nuevosConsensuados);
    // }
  }
}
