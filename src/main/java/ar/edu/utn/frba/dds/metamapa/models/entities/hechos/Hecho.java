package ar.edu.utn.frba.dds.metamapa.models.entities.hechos;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.converters.LocalDateTimeConverter;
import ar.edu.utn.frba.dds.metamapa.models.entities.Persistente;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Origen;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hecho")
public class Hecho extends Persistente {

  @Column(name = "titulo")
  @CsvBindByName(column = "Título")
  private String titulo;

  @Column(name = "descripcion")
  @CsvBindByName(column = "Descripción")
  private String descripcion;

  @Column(name = "categoria")
  @CsvBindByName(column = "Categoría")
  private String categoria;

  @Column(name = "latitud")
  @CsvBindByName(column = "Latitud")
  private Double latitud;

  @Column(name = "longitud")
  @CsvBindByName(column = "Longitud")
  private Double longitud;

  @Column(name = "fecha_acontecimiento")
  @CsvCustomBindByName(column = "Fecha del hecho", converter = LocalDateTimeConverter.class)
  private LocalDateTime fechaAcontecimiento;

  @Column(name = "fecha_carga")
  @Builder.Default
  private LocalDateTime fechaCarga = LocalDateTime.now();

  @Builder.Default
  @ElementCollection
  @CollectionTable(name = "hecho_etiqueta", joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"))
  @Column(name = "etiqueta")
  private List<String> etiquetas = new ArrayList<>();

  @Column(name = "eliminado")
  @Builder.Default
  private Boolean eliminado = false;

  @Enumerated(EnumType.STRING)
  @Column(name = "origen")
  private Origen origen;

  // lista de paths al filesystem
  @ElementCollection
  @CollectionTable(name = "hecho_multimedia", joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"))
  @Column(name = "multimedia")
  private List<String> multimedia;

  @ManyToOne
  @JoinColumn(name = "contribuyente_id")
  private Contribuyente contribuyente;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado")
  private Estado estado;

  @Column(name = "limite_dias_edicion")
  private Long limiteDiasEdicion;

  @Embedded
  private Ubicacion ubicacion;

  public boolean esEditable() {
    if (this.contribuyente == null || this.contribuyente.isEsAnonimo()) {
      return false;
    }
    long diasDesdeCarga = ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now());
    return diasDesdeCarga <= limiteDiasEdicion;
  }

  public void agregarEtiqueta(String etiqueta) {
    this.etiquetas.add(etiqueta);
  }

  public void agregarMultimedia(String path) {
    this.multimedia.add(path);
  }

  public void actualizarHecho(Hecho hecho) {
    if (!esEditable()) {
      throw new IllegalStateException("El hecho no es editable.");
    }
    this.setDescripcion(hecho.getDescripcion());
    this.setCategoria(hecho.getCategoria());
    this.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
    this.setMultimedia(hecho.getMultimedia());
    this.getEtiquetas().clear();
    this.getEtiquetas().addAll(hecho.getEtiquetas());
  }

  public void eliminar() {
    this.eliminado = true;
  }

  public void aceptar() {
    this.estado = Estado.ACEPTADA;
  }

  public void rechazar() {
    this.estado = Estado.RECHAZADA;
  }

}