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

  @Override
  public void agregarFuenteAColeccion(String handleColeccion, Long idFuente) {
    Coleccion coleccion = this.coleccionesRepository.findColeccionByHandle(handleColeccion);
    Fuente fuente = this.fuentesRepository.findFuenteById(idFuente);
    if (coleccion == null || fuente == null) {
      throw new IllegalArgumentException("Coleccion o fuente no encontrada");
    }
    coleccion.agregarFuente(fuente);
    this.coleccionesRepository.save(coleccion);
  }

  @Override
  public void eliminarFuenteDeColeccion(String handleColeccion, Long idFuente) {
    Coleccion coleccion = this.coleccionesRepository.findColeccionByHandle(handleColeccion);
    Fuente fuente = this.fuentesRepository.findFuenteById(idFuente);
    coleccion.eliminarFuente(fuente);
    this.coleccionesRepository.save(coleccion);
  }

  @Override
  public void crearSolicitud(SolicitudEliminacionInputDTO solicitudDto) {
    this.hechosRepository.findById(solicitudDto.getIdHecho()).ifPresent(
        hecho -> {
          var solicitud = new SolicitudEliminacion(
              hecho,
              solicitudDto.getRazon());
          if (detectorDeSpam.esSpam(solicitudDto.getRazon())) {
            solicitud.rechazarSolicitud();
            solicitud.marcarSpam();
          }
          this.solicitudesRepository.save(solicitud);
        }
    );
  }

  public void refrescarColecciones() {
    List<Coleccion> colecciones = this.coleccionesRepository.findAll();
    colecciones.forEach(Coleccion::actualizarColeccion);
    this.coleccionesRepository.saveAll(colecciones);
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

