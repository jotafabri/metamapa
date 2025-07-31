package ar.edu.utn.frba.dds.metamapa.models.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contribuyente {
  private String nombre;
  private String apellido;
  private int edad;
  private boolean esAnonimo;

  public Contribuyente(String nombre, String apellido, int edad, boolean esAnonimo) {
    if (nombre == null || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("El campo nombre es obligatorio.");
    }
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
    this.esAnonimo = esAnonimo;
  }

  @Override
  public String toString() {
    return esAnonimo ? "Anónimo" : this.nombre + " " + this.apellido;
  }
}

