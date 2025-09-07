package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.SolicitudEliminacion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IFuentesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.impl.ColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import ar.edu.utn.frba.dds.metamapa.services.IDetectorSpam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgregacionService implements IAgregacionService {

  @Autowired
  private IColeccionesRepository coleccionesRepository;
  @Autowired
  private ISolicitudesEliminacionRepository solicitudesRepository;
  @Autowired
  private IHechosRepository hechosRepository;
  @Autowired
  private IFuentesRepository fuentesRepository;
  @Autowired
  private IDetectorSpam detectorDeSpam;

  public AgregacionService(ColeccionesRepository mockColeccionRepo) {
  }

  @Override
  public void agregarFuenteAColeccion(String handleColeccion, Long idFuente) {
    Coleccion coleccion = this.coleccionesRepository.findByHandle(handleColeccion);
    Fuente fuente = this.fuentesRepository.findById(idFuente);
    if (coleccion == null || fuente == null) {
      throw new IllegalArgumentException("Coleccion o fuente no encontrada");
    }
    coleccion.agregarFuente(fuente);
  }

  @Override
  public void eliminarFuenteDeColeccion(String handleColeccion, Long idFuente) {
    Coleccion coleccion = this.coleccionesRepository.findByHandle(handleColeccion);
    Fuente fuente = this.fuentesRepository.findById(idFuente);
    coleccion.eliminarFuente(fuente);
  }

  @Override
  public void crearSolicitud(SolicitudEliminacionInputDTO solicitudDto) {
    var hecho = this.hechosRepository.findById(solicitudDto.getIdHecho());
    if (hecho != null) {
      var solicitud = new SolicitudEliminacion(
          hecho,
          solicitudDto.getRazon());
      if (detectorDeSpam.esSpam(solicitudDto.getRazon())) {
        solicitud.rechazarSolicitud();
      }
      this.solicitudesRepository.save(solicitud);
    }
  }

  public void refrescarColecciones() {
    for (Coleccion coleccion : this.obtenerColecciones()) {
      coleccion.actualizarColeccion();
    }
  }

  @Override
  public List<Coleccion> obtenerColecciones() {
    return coleccionesRepository.findAll();
  }


  @Override
  public void aprobarSolicitudById(Long id) {
    this.solicitudesRepository.findById(id)
        .ifPresent(solicitud -> {
          if (solicitud.getEstado() != Estado.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden aprobar solicitudes pendientes. Estado actual: " + solicitud.getEstado());
          }
          if (solicitud.getHecho() == null) {
            throw new IllegalStateException("La solicitud no tiene un hecho asociado válido");
          }
          solicitud.aceptarSolicitud();
          this.solicitudesRepository.save(solicitud);
        });
  }


  @Override
  public void rechazarSolicitudById(Long id) {
    this.solicitudesRepository.findById(id)
        .ifPresent(solicitud -> {
          if (solicitud.getEstado() != Estado.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden aprobar solicitudes pendientes. Estado actual: " + solicitud.getEstado());
          }
          if (solicitud.getHecho() == null) {
            throw new IllegalStateException("La solicitud no tiene un hecho asociado válido");
          }
          solicitud.rechazarSolicitud();
          this.solicitudesRepository.save(solicitud);
        });
  }

  public List<SolicitudEliminacionOutputDTO> findAllSolicitudes() {
    return this.solicitudesRepository.findAll().stream().map(SolicitudEliminacionOutputDTO::fromSolicitud).toList();
  }

}

