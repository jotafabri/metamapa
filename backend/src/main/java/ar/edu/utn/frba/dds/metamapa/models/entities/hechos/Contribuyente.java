package ar.edu.utn.frba.dds.metamapa.models.entities.hechos;

import java.time.LocalDate;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Persistente;
import ar.edu.utn.frba.dds.metamapa.models.entities.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "contribuyente")
public class Contribuyente extends Persistente {

  @OneToOne
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

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

  public static Contribuyente buildAnonimo() {
    Contribuyente contribuyente = new Contribuyente();
    contribuyente.setNombre("Anónimo");
    contribuyente.setEsAnonimo(true);
    return contribuyente;
  }

  @Override
  public String toString() {
    return esAnonimo ? "Anónimo" : this.nombre + " " + this.apellido;
  }
}

