package ar.edu.utn.frba.dds.metamapa.services;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.SolicitudEliminacionOutputDTO;

public interface IAgregacionService {

  void agregarFuenteAColeccion(String handleColeccion, Long idFuente);

  void eliminarFuenteDeColeccion(String handleColeccion, Long idFuente);

  void refrescarColecciones();

  //void refrescarHechosColeccion(Coleccion coleccion);

  //Solicitudes de eliminacion

  SolicitudEliminacionOutputDTO crearSolicitud(Long hechoId, String razon);

  List<SolicitudEliminacionOutputDTO> findAllSolicitudes();

  void aprobarSolicitudById(Long id);

  void rechazarSolicitudById(Long id);
}
