package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Hecho {

  private int limiteDiasEdicion;

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

  private LocalDateTime fechaCarga;
  private Origen origen;
  private Multimedia multimedia = null;
  private List<String> etiquetas = new ArrayList();
  private Boolean eliminado = false;
  private Contribuyente contribuyente = null;
  private EstadoHecho estado = EstadoHecho.ACEPTADO;

  public Hecho() {}

  public Hecho(String titulo, String descripcion, String categoria, Double latitud, Double longitud, LocalDateTime fechaAcontecimiento, Origen origen) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.latitud = latitud;
    this.longitud = longitud;
    this.fechaAcontecimiento = fechaAcontecimiento;
    this.fechaCarga = LocalDateTime.now();
    this.origen = origen;
  }

  public void agregarEtiqueta(String etiqueta) {
    this.etiquetas.add(etiqueta);
  }

  public boolean esEditable() {
    if (this.contribuyente == null || this.contribuyente.isEsAnonimo()) {
      return false;
    }
    long diasDesdeCarga = ChronoUnit.DAYS.between(this.fechaCarga, LocalDateTime.now());
    return diasDesdeCarga <= limiteDiasEdicion;
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

  public void eliminar(){
    this.eliminado = true;
  }

  public void aceptar() {
    this.estado = EstadoHecho.ACEPTADO;
  }

  public void rechazar() {
    this.estado = EstadoHecho.RECHAZADO;
  }

}