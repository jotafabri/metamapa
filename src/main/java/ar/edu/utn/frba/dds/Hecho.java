package ar.edu.utn.frba.dds;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Hecho {
  private String titulo;
  private String descripcion;
  private String categoria; //esto va a ser una clase
  private Coordenadas coordenadas;
  private LocalDateTime fechaAcontecimiento;
  private LocalDateTime fechaCarga;
  private Origen origen;

  public Hecho(String titulo, String descripcion, String categoria, Coordenadas coordenadas, LocalDateTime fechaAcontecimiento, Origen origen) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.coordenadas = coordenadas;
    this.fechaAcontecimiento = fechaAcontecimiento;
    this.fechaCarga = LocalDateTime.now();
    this.origen = origen;
  }

  public String printHecho() {
    return String.format("Titulo: %s\nDescripcion: %s\nCategoria: %s\nCoordenadas: %s, %s\nFecha: %s\n", this.titulo, this.descripcion,this.categoria, this.coordenadas.getLatitud(), this.coordenadas.getLongitud(), this.fechaAcontecimiento);
  }
}
