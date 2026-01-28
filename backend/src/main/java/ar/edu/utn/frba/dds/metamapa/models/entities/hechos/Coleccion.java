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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "coleccion")
public class Coleccion extends Persistente {

  private static final Logger log = LoggerFactory.getLogger(Coleccion.class);

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

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
          name = "coleccion_hecho_curado",
          joinColumns = @JoinColumn(name = "coleccion_id"),
          inverseJoinColumns = @JoinColumn(name = "hecho_id")
  )
  private List<Hecho> hechosCurados = new ArrayList<>();


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

    // Usamos lista para permitir repetidos
    List<Hecho> nuevosHechos = fuentes.stream()
            .flatMap(fuente -> fuente.getHechos().stream())
            .filter(h -> sinCriterios || this.criterios.stream().allMatch(c -> c.cumple(h)))
            .toList();

    // Siempre actualizamos la lista
    this.hechos.clear();
    this.hechos.addAll(nuevosHechos);
  }


  public List<Hecho> navegar(List<Filtro> criterios, Boolean curado) {
    // Usamos la lista persistida de curados si curado=true
    var lista = curado ? this.hechosCurados : this.hechos;
    return lista.stream()
            .filter(h -> criterios.isEmpty() || criterios.stream().allMatch(c -> c.cumple(h)))
            .toList();
  }


  public void actualizarCurados() {
    log.info("Actualizando curados de la colección '{}' con algoritmo: {}", this.handle, algoritmoDeConsenso.getNombre());

    // Filtramos los hechos según la estrategia de consenso
    List<Hecho> curados = this.algoritmoDeConsenso.filtrarConsensuados(this.hechos, this.fuentes);

    // Limpiamos y persistimos en la relación ManyToMany
    this.hechosCurados.clear();
    this.hechosCurados.addAll(curados);

    log.info("Curados actualizados de la colección '{}': {} elementos", this.handle, curados.size());

    // Logs detallados de cada hecho curado
    for (Hecho h : curados) {
      log.info("Colección '{}', Curado: titulo='{}', categoria='{}', descripcion='{}'",
              this.handle, h.getTitulo(), h.getCategoria(), h.getDescripcion());
    }
  }


}
