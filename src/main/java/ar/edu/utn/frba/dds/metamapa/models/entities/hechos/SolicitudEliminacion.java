package ar.edu.utn.frba.dds.metamapa.models.entities.hechos;

import ar.edu.utn.frba.dds.metamapa.models.entities.Persistente;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "solicitud_eliminacion")
public class SolicitudEliminacion extends Persistente {

  @ManyToOne
  @JoinColumn(name = "hecho_id")
  private Hecho hecho;

  @Column(name = "causa")
  private String causa;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado")
  private Estado estado = Estado.PENDIENTE;

  @Column(name = "es_spam")
  private Boolean esSpam = false;

  public SolicitudEliminacion(Hecho hecho, String causa) {
    this.hecho = hecho;
    this.causa = causa;
  }

  public void aceptarSolicitud() {
    this.estado = Estado.ACEPTADA;
    this.hecho.eliminar();
  }

  public void rechazarSolicitud() {
    this.estado = Estado.RECHAZADA;
  }

  public void marcarSpam() {
    this.esSpam = true;
  }

}
