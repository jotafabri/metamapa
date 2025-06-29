package ar.edu.utn.frba.dds.metamapa.services;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.SolicitudEliminacion;

public interface IAgregacionService {

    void agregarFuenteAColeccion(String handleColeccion, Long idFuente);

    void eliminarFuenteDeColeccion(String handleColeccion, Long idFuente);

    void refrescarColecciones();

    List<Coleccion> obtenerColecciones();

    //void refrescarHechosColeccion(Coleccion coleccion);

    //Solicitudes de eliminacion

    void crearSolicitud(SolicitudEliminacionDTO solicitud);

    List<SolicitudEliminacionOutputDTO> findAllSolicitudes();

    void aprobarSolicitudById(Long id);
    void rechazarSolicitudById(Long id);
}
