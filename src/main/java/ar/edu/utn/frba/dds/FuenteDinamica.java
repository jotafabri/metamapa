package ar.edu.utn.frba.dds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

@Getter
public class FuenteDinamica extends Fuente {
  public List<SolicitudEliminacion> solicitudesEliminacion = new ArrayList<>();

  void agregarHecho(Hecho hecho) {
    for (Hecho hecho2 : listaHechos) { // find
      if (hecho2.getTitulo().equalsIgnoreCase(hecho.getTitulo())) {
        hecho2.setDescripcion(hecho.getDescripcion());
        hecho2.setCategoria(hecho.getCategoria());
        hecho2.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
        hecho2.setFechaCarga(hecho.getFechaCarga());
        hecho2.setOrigen(hecho.getOrigen());
        return;
      }
    }
    listaHechos.add(hecho);
  }

  public void generarSolicitudEliminacionPorTitulo(String titulo, String causa) {
    Hecho hecho = this.buscarHechoPorTitulo(titulo);
    if (hecho != null) {
      SolicitudEliminacion solicitud = new SolicitudEliminacion(hecho, causa);
      solicitudesEliminacion.add(solicitud);
    } else {
      System.out.println("Por favor ingrese un título de hecho válido.");
    }
    ;
  }

  public void aceptarSolicitudPorTitulo(String titulo) {
    for (SolicitudEliminacion solicitud : solicitudesEliminacion) {
      if (Objects.equals(titulo, solicitud.getHecho().getTitulo())) {
        aceptarSolicitud(solicitud);
      }
    }
  }

  public void rechazarSolicitudPorTitulo(String titulo) {
    for (SolicitudEliminacion solicitud : solicitudesEliminacion) {
      if (Objects.equals(titulo, solicitud.getHecho().getTitulo())) {
        rechazarSolicitud(solicitud);
      }
    }
  }

  public void aceptarSolicitud(SolicitudEliminacion solicitud) {
    Hecho hechoAEliminar = solicitud.getHecho();
    //cambiar por un booleano que diga si esta eliminado o no
    listaHechos.removeIf(h -> h.getTitulo().equalsIgnoreCase(hechoAEliminar.getTitulo()));
    solicitud.aceptarSolicitud();
    // solicitudesEliminacion.remove(solicitud);
  }

  public void rechazarSolicitud(SolicitudEliminacion solicitud) {
    solicitud.rechazarSolicitud();
    // solicitudesEliminacion.remove(solicitud);
  }

  public void listarSolicitudesEliminacion() {
    for (SolicitudEliminacion solicitud : solicitudesEliminacion) {
      System.out.println(solicitud.consultarSolicitud());
    }
  }
}
