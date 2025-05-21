package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.SolicitudEliminacion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AgregacionService implements IAgregacionService {

  @Autowired
  private IColeccionesRepository coleccionRepository;
  @Autowired
  private ISolicitudesEliminacionRepository solicitudesRepository;
  @Autowired
  private IHechosRepository hechosRepository;

  @Override
  public void crearSolicitud(SolicitudEliminacionDTO solicitudDto) {
    var hecho = this.hechosRepository.findById(solicitudDto.getIdHecho());
    if (hecho != null) {
      var solicitud = new SolicitudEliminacion(
          hecho,
          solicitudDto.getRazon());
      this.solicitudesRepository.save(solicitud);
    }
  }

  //private final HechoRepository hechoRepo;
  // Esto lo tiene que hacer juan ignacio
  /*@Scheduled(fixedRate = 3600000)
  @Override
  public void refrescarColecciones() {
    for (Coleccion coleccion : this.obtenerColecciones()) {
      refrescarHechosColeccion(coleccion);
    }
  }*/

  //TODO
  @Override
  public List<Coleccion> obtenerColecciones() {
    return coleccionRepository.findAll();
  }

  //@Override
  /*private void refrescarHechosColeccion(Coleccion coleccion) {


  }*/


  @Override
  public void aprobarSolicitud(SolicitudEliminacion solicitud) {

    if (solicitud.getEstado() != Estado.PENDIENTE) {
      throw new IllegalStateException("Solo se pueden aprobar solicitudes pendientes. Estado actual: " + solicitud.getEstado());
    }
    if (solicitud.getHecho() == null) {
      throw new IllegalStateException("La solicitud no tiene un hecho asociado válido");
    }
    solicitud.aceptarSolicitud();
    solicitudesRepository.save(solicitud);

  }

  @Override
  public void rechazarSolicitud(SolicitudEliminacion solicitud) {

    if (solicitud.getEstado() != Estado.PENDIENTE) {
      throw new IllegalStateException("Solo se pueden rechazar solicitudes pendientes");
    }

    if (solicitud.getHecho() == null) {
      throw new IllegalStateException("La solicitud no tiene un hecho asociado válido");
    }

    solicitud.rechazarSolicitud();
    solicitudesRepository.save(solicitud);
  }

  public void findAllSolicitudes(){
    this.solicitudesRepository.findAll();
  }


}

