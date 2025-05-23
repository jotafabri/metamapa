package ar.edu.utn.frba.dds.metamapa.models.entities;

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
  private boolean visible;

  private Long id;
  private String titulo;
  private String descripcion;
  private Categoria categoria;
  private Coordenada coordenada;
  private Contribuyente contribuyente;
  private Multimedia multimedia;
  private final LocalDateTime fechaCarga;
  private LocalDateTime fechaAcontecimiento;
  private List<String> etiquetas = new ArrayList<>();

  private EstadoHecho estado = EstadoHecho.EN_REVISION;


  public Hecho() {
    this(LocalDateTime.now());
  }

//Sobrecarga de contructor para testeos
  public Hecho(LocalDateTime fechaCarga) {
    this.fechaCarga = fechaCarga;
  }


  public LocalDateTime getFechaCarga() {
    return this.fechaCarga;
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



  @Override
  public String toString() {
    String contribuyenteInfo = this.contribuyente != null ? this.contribuyente.getNombre() : "Anónimo";
    return String.format(
            "Titulo: %s\nDescripcion: %s\nCategoria: %s\nCoordenadas: %s, %s\nFecha Acontecimiento: %s\nFecha Carga: %s\nVisible: %b\nContribuyente: %s\n",
            this.titulo, this.descripcion, this.categoria,
            this.coordenada.getLatitud(), this.coordenada.getLongitud(),
            this.fechaAcontecimiento, this.fechaCarga,
            this.visible, contribuyenteInfo
    );
}

  public void aceptar() {
    this.estado = EstadoHecho.ACEPTADO;
  }

  public void rechazar() {
    this.estado = EstadoHecho.RECHAZADO;
  }

}