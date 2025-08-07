package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Origen;
import ar.edu.utn.frba.dds.metamapa.models.entities.utils.LocalDateTimeConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
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
public class Hecho {
  private Long id;

  @CsvBindByName(column = "Título")
  private String titulo;

  @CsvBindByName(column = "Descripción")
  private String descripcion;

  @CsvBindByName(column = "Categoría")
  private String categoria;

  @CsvBindByName(column = "Latitud")
  private Double latitud;

  @CsvBindByName(column = "Longitud")
  private Double longitud;

  @CsvCustomBindByName(column = "Fecha del hecho", converter = LocalDateTimeConverter.class)
  private LocalDateTime fechaAcontecimiento;

  @Builder.Default
  private LocalDateTime fechaCarga = LocalDateTime.now();

  @Builder.Default
  private List<String> etiquetas = new ArrayList<>();

  @Builder.Default
  private Boolean eliminado = false;

  private Origen origen;
  private Multimedia multimedia;
  private Contribuyente contribuyente;
  private Estado estado;
  private Long limiteDiasEdicion;

  public boolean esEditable() {
    if (this.contribuyente == null || this.contribuyente.isEsAnonimo()) {
      return false;
    }
    long diasDesdeCarga = ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now());
    return diasDesdeCarga <= limiteDiasEdicion;
  }

  public Hecho agregarEtiqueta(String etiqueta) {
    this.etiquetas.add(etiqueta);
    return this;
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