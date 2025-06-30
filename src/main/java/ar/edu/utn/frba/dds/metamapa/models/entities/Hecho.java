package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Origen;
import ar.edu.utn.frba.dds.metamapa.models.entities.utils.LocalDateTimeConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.time.temporal.ChronoUnit;

@Getter
@Setter
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

  private LocalDateTime fechaCarga;
  private Origen origen;
  private Multimedia multimedia;
  private Contribuyente contribuyente;

  private List<String> etiquetas;
  private Boolean eliminado;
  private Estado estado;

  @Value("${limite.dias.edicion}")
  private Long limiteDiasEdicion;

  public Hecho() {
    this.fechaCarga = LocalDateTime.now();
    this.etiquetas = new ArrayList();
    this.eliminado = false;
    this.estado = Estado.PENDIENTE;
  }

  public Hecho(String titulo, String descripcion, String categoria, Double latitud, Double longitud, LocalDateTime fechaAcontecimiento) {
    this();
    if (titulo == null || descripcion == null || categoria == null || latitud == null || longitud == null || fechaAcontecimiento == null) {
      throw new IllegalArgumentException("Los campos título, descripción, categoría, coordenadas, fechaAcontecimiento son obligatorios.");
    }
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.latitud = latitud;
    this.longitud = longitud;
    this.fechaAcontecimiento = fechaAcontecimiento;
  }

  public Hecho conMultimedia(Multimedia multimedia) {
    this.multimedia = multimedia;
    return this;
  }

  public Hecho conContribuyente(Contribuyente contribuyente) {
    this.contribuyente = contribuyente;
    return this;
  }

  public Hecho agregarEtiqueta(String etiqueta) {
    this.etiquetas.add(etiqueta);
    return this;
  }

  public Hecho conLimiteDiasEdicion(Long limiteDias) {
    this.limiteDiasEdicion = limiteDias;
    return this;
  }

  public Hecho conEstado(Estado estado) {
    this.estado = estado;
    return this;
  }

  public Hecho conFechaCarga(LocalDateTime fechaCarga) {
    this.fechaCarga = fechaCarga;
    return this;
  }

  //Sobrecarga de contructor para testeos
  public Hecho (LocalDateTime fechaCarga) {
    this.fechaCarga = fechaCarga;
  }
  
  public Hecho build() {
    if (this.contribuyente == null) {
      throw new IllegalStateException("Se requiere un contribuyente.");
    }

    Hecho hecho = new Hecho(this.fechaCarga); // usa el constructor nuevo

    hecho.setTitulo(titulo);
    hecho.setDescripcion(descripcion);
    hecho.setCategoria(categoria);
    hecho.setLatitud(latitud);
    hecho.setLongitud(longitud);
    hecho.setFechaAcontecimiento(fechaAcontecimiento);
    hecho.setMultimedia(multimedia);
    hecho.setContribuyente(contribuyente);
    hecho.setLimiteDiasEdicion(limiteDiasEdicion);
    hecho.getEtiquetas().addAll(etiquetas);
    hecho.setEstado(estado);

    return hecho;
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
    this.estado = Estado.ACEPTADA;
  }

  public void rechazar() {
    this.estado = Estado.RECHAZADA;
  }

}