package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "contribuyente")
public class Contribuyente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nombre", nullable = false)
  private String nombre;

  @Column(name = "apellido")
  private String apellido;

  @Column(name = "fecha_nacimiento")
  private LocalDate fechaNacimiento;

  @Column(name = "anonimo")
  private boolean esAnonimo;

  @OneToMany(mappedBy = "contribuyente", fetch = FetchType.LAZY)
  private List<Hecho> hechos;

  public Contribuyente(String nombre, String apellido, LocalDate fechaNacimiento, boolean esAnonimo) {
    if (nombre == null || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("El campo nombre es obligatorio.");
    }
    this.nombre = nombre;
    this.apellido = apellido;
    this.fechaNacimiento = fechaNacimiento;
    this.esAnonimo = esAnonimo;
  }

  @Override
  public String toString() {
    return esAnonimo ? "Anónimo" : this.nombre + " " + this.apellido;
  }
}

