package ar.edu.utn.frba.dds.metamapa.models.entities.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ar.edu.utn.frba.dds.metamapa.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.Multimedia;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.EstadoHecho;

public class HechoBuilder {

  private static final int LIMITE_DIAS_EDICION_DEFAULT;
  private LocalDateTime fechaCarga = LocalDateTime.now();

  static {
    //TODO se podría pasar la config de application properties a una clase que tiene los valores de las properties
    ResourceBundle config = ResourceBundle.getBundle("application");
    LIMITE_DIAS_EDICION_DEFAULT = Integer.parseInt(config.getString("limite.dias.edicion"));
  }

  private String titulo;
  private String descripcion;
  private String categoria;
  private Double latitud;
  private Double longitud;
  private LocalDateTime fechaAcontecimiento;
  private Multimedia multimedia;
  private Contribuyente contribuyente;
  private List<String> etiquetas = new ArrayList<>();
  private int limiteDiasEdicion = LIMITE_DIAS_EDICION_DEFAULT;
  private EstadoHecho estado = EstadoHecho.EN_REVISION;

  //TODO elegir si nos quedamos con el Builder o con el Constructor para instanciar hechos en general

  public HechoBuilder(String titulo, String descripcion, String categoria, Double latitud, Double longitud, LocalDateTime fechaAcontecimiento) {
    //TODO el builder no debe recibir parámetros, sino que debe agregar valores de a poco
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

  public HechoBuilder conMultimedia(Multimedia multimedia) {
    this.multimedia = multimedia;
    return this;
  }

  public HechoBuilder conContribuyente(Contribuyente contribuyente) {
    this.contribuyente = contribuyente;
    return this;
  }

  public HechoBuilder agregarEtiqueta(String etiqueta) {
    this.etiquetas.add(etiqueta);
    return this;
  }


  public HechoBuilder conLimiteDiasEdicion(int limiteDias) {
    this.limiteDiasEdicion = limiteDias;
    return this;
  }

  public HechoBuilder conEstado(EstadoHecho estado) {
    this.estado = estado;
    return this;
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

  public HechoBuilder conFechaCarga(LocalDateTime fechaCarga) {
    this.fechaCarga = fechaCarga;
    return this;
  }
}
