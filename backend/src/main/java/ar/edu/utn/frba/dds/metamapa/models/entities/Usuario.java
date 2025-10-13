package ar.edu.utn.frba.dds.metamapa.models.entities;

import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Rol;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

  public Usuario(String email, String password, Rol rol) {
    super();
    this.email = email;
    this.password = password;
    this.rol = rol;
  }
}