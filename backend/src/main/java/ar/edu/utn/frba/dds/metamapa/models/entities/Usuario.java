package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Permiso;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Rol;
import jakarta.persistence.*;
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

  @ElementCollection(targetClass = Permiso.class, fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  @CollectionTable(
          name = "usuario_permisos",
          joinColumns = @JoinColumn(name = "usuario_id")
  )
  @Column(name = "permiso")
  private List<Permiso> permisos;

  public Usuario(String email, String password, Rol rol, String nombre, String apellido, List<Permiso> permisos) {
    super();
    this.email = email;
    this.password = password;
    this.rol = rol;
    this.nombre = nombre;
    this.apellido = apellido;
    this.permisos = permisos;
  }

  public Usuario(String email, String password, Rol rol) {
    super();
    this.email = email;
    this.password = password;
    this.rol = rol;
    this.permisos = new ArrayList<>();
  }


  @Override
  public String toString() {
    return this.nombre + (this.apellido != null ? " " + this.apellido : "");
  }
}