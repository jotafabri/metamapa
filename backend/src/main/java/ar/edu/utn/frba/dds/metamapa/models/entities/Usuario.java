package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDate;

import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Rol;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario extends Persistente {

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "rol", nullable = false)
  private Rol rol;

  @Column(name = "nombre", nullable = false)
  private String nombre;

  @Column(name = "apellido")
  private String apellido;

  @Column(name = "fecha_nacimiento")
  private LocalDate fechaNacimiento;

  public Usuario(String email, String password, Rol rol) {
    super();
    this.email = email;
    this.password = password;
    this.rol = rol;
  }

  @Override
  public String toString() {
    return this.nombre + (this.apellido != null ? " " + this.apellido : "");
  }
}