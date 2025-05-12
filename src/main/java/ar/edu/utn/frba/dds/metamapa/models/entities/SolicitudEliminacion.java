package ar.edu.utn.frba.dds.metamapa.models.entities;

import lombok.Getter;

@Getter
public class SolicitudEliminacion {
  private Hecho hecho;
  private String causa;
  private Estado estado = Estado.PENDIENTE;

  public SolicitudEliminacion(Hecho hecho, String causa) {
    this.hecho = hecho;
    this.causa = causa;
  }

  public void aceptarSolicitud() {
    this.estado = Estado.ACEPTADA;
    hecho.eliminar();
  }

  public void rechazarSolicitud() {
    this.estado = Estado.RECHAZADA;
  }

}
