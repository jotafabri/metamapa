package ar.edu.utn.frba.dds;
import lombok.Getter;

@Getter
public class SolicitudEliminacion {
  private Hecho hecho;
  private String causa;
  private Estado estado = Estado.pendiente;


  public SolicitudEliminacion(Hecho hecho, String causa){
    this.hecho = hecho;
    this.causa = causa;
  }

  public void aceptarSolicitud(){
    this.estado = Estado.aceptada;
  }
  public void rechazarSolicitud(){
    this.estado = Estado.rechazada;
  }
  public String consultarSolicitud(){
    return String.format("Se ha solicitado eliminar este hecho:\nTitulo: %s\nCausa: %s\nEstado: %s",
        hecho.getTitulo(),
    causa, this.getEstado());
  }
}
