package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class Persistente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fecha_alta")
  private LocalDateTime fechaAlta;

  @Column(name = "fecha_modificacion")
  private LocalDateTime fechaModificacion;

  public Persistente() {
    this.fechaAlta = LocalDateTime.now();
  }

  protected void setModificado() {
    this.fechaModificacion = LocalDateTime.now();
  }
}
