package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

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
  private Float latitud;

  @CsvBindByName(column = "Longitud")
  private Float longitud;

  @CsvBindByName(column = "Fecha del hecho")
  private LocalDateTime fechaAcontecimiento;
  private LocalDateTime fechaCarga;
  private Origen origen;
  private Multimedia multimedia = null;
  public List<String> etiquetas = new ArrayList();
  private Boolean eliminado = false;

  public Hecho(String titulo, String descripcion, String categoria, Float latitud, Float longitud, LocalDateTime fechaAcontecimiento, Origen origen) {
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
    etiquetas.add(etiqueta);
  }

  public void actualizarHecho(Hecho hecho){
    this.setDescripcion(hecho.getDescripcion());
    this.setCategoria(hecho.getCategoria());
    this.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
    this.setFechaCarga(hecho.getFechaCarga());
    this.setOrigen(hecho.getOrigen());
  }

  public void eliminar(){
    this.eliminado = true;
  }

}
