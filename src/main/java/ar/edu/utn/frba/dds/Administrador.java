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
                .filter(s -> s.getEstado() == Estado.pendiente)
                .toList();

        if (solicitudesPendientes.isEmpty()) {
            System.out.println("No hay solicitudes pendientes.");
            return;
        }

        for (SolicitudEliminacion solicitud : solicitudesPendientes) {
            System.out.println(solicitud.consultarSolicitud());

            // Simulamos una decisión por consola
            if (decidirAceptar()) {
                fuente.aceptarSolicitud(solicitud);
                System.out.println("✅ Solicitud aceptada y hecho eliminado.");
            } else {
                fuente.rechazarSolicitud(solicitud);
                System.out.println("❌ Solicitud rechazada.");
            }
            System.out.println("-----------------------");
        }
    }

    // Método auxiliar para simular decisiones por consola
    private boolean decidirAceptar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("¿Aceptar esta solicitud? (s/n)");
        String input = scanner.nextLine();
        return(input.equalsIgnoreCase("s"));
    }
}
