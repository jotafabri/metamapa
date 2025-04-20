package ar.edu.utn.frba.dds;

import java.util.ArrayList;
import java.util.List;

public class FuenteDinamica extends Fuente {
    public List<SolicitudEliminacion> solicitudesEliminacion = new ArrayList();
    void agregarHecho (Hecho hecho){
        for(Hecho hecho2 : listaHechos){
            if(hecho2.getTitulo().equalsIgnoreCase(hecho.getTitulo())){
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

    public void consultarSolicitudesPendientes() {
        for (SolicitudEliminacion s : solicitudesEliminacion) {
            System.out.println(s.consultarSolicitud());
            System.out.println("---------------");
        }
    }
    public void agregarSolicitud(SolicitudEliminacion solicitud) {
        solicitudesEliminacion.add(solicitud);
    }
    public void aceptarSolicitud(SolicitudEliminacion solicitud) {
        Hecho hechoAEliminar = solicitud.getHecho();
        listaHechos.removeIf(h -> h.getTitulo().equalsIgnoreCase(hechoAEliminar.getTitulo()));
        solicitudesEliminacion.remove(solicitud);
    }
    public void rechazarSolicitud(SolicitudEliminacion solicitud) {
        solicitudesEliminacion.remove(solicitud);
    }

    public List<SolicitudEliminacion> getSolicitudesEliminacion() {
        return solicitudesEliminacion;
    }
}
