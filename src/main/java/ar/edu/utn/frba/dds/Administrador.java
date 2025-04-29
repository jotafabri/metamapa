package ar.edu.utn.frba.dds;

import java.util.List;
import java.util.Scanner;

public class Administrador extends Visitante {
  private String nombre;

  public Administrador(String nombre) {
    this.nombre = nombre;
  }

  public void revisarSolicitudes(FuenteDinamica fuente) {
    List<SolicitudEliminacion> solicitudes = fuente.getSolicitudesEliminacion();
    List<SolicitudEliminacion> solicitudesPendientes = solicitudes.stream()
        .filter(s -> s.getEstado() == Estado.PENDIENTE)
        .toList();

    if (solicitudesPendientes.isEmpty()) {
      return;
    }

    for (SolicitudEliminacion solicitud : solicitudesPendientes) {
      System.out.println(solicitud.consultarSolicitud());

      // Simulamos una decisión por consola
      if (decidirAceptar()) {
        fuente.aceptarSolicitud(solicitud);
      } else {
        fuente.rechazarSolicitud(solicitud);
      }
    }
  }

  private boolean decidirAceptar() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("¿Aceptar esta solicitud? (s/n)");
    String input = scanner.nextLine();
    return (input.equalsIgnoreCase("s"));
  }
}
