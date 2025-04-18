package ar.edu.utn.frba.dds;

public class SolicitudEliminacion {
  private Hecho hecho;
  private String causa;

  public SolicitudEliminacion(Hecho hecho, String causa){
    this.hecho = hecho;
    this.causa = causa;
  }

  public String consultarSolicitud(){
    return String.format("Se ha solicitad eliminar este hecho:\nTitulo: %s\nDescripcion: %s\nCategoria: %s\nCoordenadas: %s, %s\nFecha: %s\nCausa: %s",
        hecho.getTitulo(), hecho.getDescripcion(),hecho.getCategoria(), hecho.getCoordenadas().getLatitud(), hecho.getCoordenadas().getLongitud(), hecho.getFechaAcontecimiento(),
    causa);
  }
  public void aceptarSolicitud(){}
  public void rechazarSolicitud(){}

}
