package ar.edu.utn.frba.dds.metamapa.services;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.controllers.SolicitudesController;
import ar.edu.utn.frba.dds.metamapa.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;

public interface IAgregacionService {

  void agregarFuenteAColeccion(String handleColeccion, Long idFuente);

  void eliminarFuenteDeColeccion(String handleColeccion, Long idFuente);

  void refrescarColecciones();

  //void refrescarHechosColeccion(Coleccion coleccion);

  //Solicitudes de eliminacion

  SolicitudEliminacionOutputDTO crearSolicitud(SolicitudEliminacionInputDTO solicitud);

  List<SolicitudEliminacionOutputDTO> findAllSolicitudes();

  void aprobarSolicitudById(Long id);

  void rechazarSolicitudById(Long id);
}
