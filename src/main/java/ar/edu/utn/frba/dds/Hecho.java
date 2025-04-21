package ar.edu.utn.frba.dds;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
  private Multimedia multimedia = null;
  public List<String> etiquetas = new ArrayList();

  public Hecho(String titulo, String descripcion, String categoria, Coordenadas coordenadas, LocalDateTime fechaAcontecimiento, Origen origen) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.coordenadas = coordenadas;
    this.fechaAcontecimiento = fechaAcontecimiento;
    this.fechaCarga = LocalDateTime.now();
    this.origen = origen;
  }

  public void agregarEtiqueta(String etiqueta){
    etiquetas.add(etiqueta);
  }
  public String printHecho() {
    return String.format("Titulo: %s\nDescripcion: %s\nCategoria: %s\nCoordenadas: %s, %s\nFecha: %s\n", this.titulo, this.descripcion,this.categoria, this.coordenadas.getLatitud(), this.coordenadas.getLongitud(), this.fechaAcontecimiento);
  }
  public void printEtiquetas(){
    System.out.println("Estas son las etiquetas que tiene el hecho:");
    for (String etiqueta : etiquetas){
      System.out.println(etiqueta);
    }
  }
}
