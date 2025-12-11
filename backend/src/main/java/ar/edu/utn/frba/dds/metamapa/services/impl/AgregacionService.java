package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.SolicitudEliminacion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IFuentesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import ar.edu.utn.frba.dds.metamapa.services.IDetectorSpam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public SolicitudEliminacionOutputDTO crearSolicitud(Long hechoId, String razon) {
    Hecho hecho = hechosRepository.findById(hechoId).orElseThrow(() -> new NotFoundException("Hecho", hechoId.toString()));
    var solicitud = new SolicitudEliminacion(
        hecho,
        razon);
    if (detectorDeSpam.esSpam(razon)) {
      solicitud.rechazarSolicitud();
      solicitud.marcarSpam();
    }
    this.solicitudesRepository.save(solicitud);
    return SolicitudEliminacionOutputDTO.fromSolicitud(solicitud);
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
          Hecho hecho = solicitud.aceptarSolicitud();
          hechosRepository.save(hecho);
          solicitudesRepository.save(solicitud);
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
    List<SolicitudEliminacion> solicitudes = solicitudesRepository.findAllPendientes();
    return solicitudes.stream().map(SolicitudEliminacionOutputDTO::fromSolicitud).toList();
  }


  @Override
  @Transactional
  public void sincronizarFuentesColeccion(String handleColeccion, List<Long> idsFuentesDeseadas) {
    Coleccion coleccion = coleccionesRepository.findColeccionByHandle(handleColeccion);
    if (coleccion == null) {
      throw new NotFoundException("Coleccion", handleColeccion);
    }

    // Delete manual en tabla join
    coleccionesRepository.deleteAllFuentesByColeccionId(coleccion.getId());

    // Insert manual en tabla join
    idsFuentesDeseadas.forEach(fuenteId ->
        coleccionesRepository.insertFuenteForColeccion(coleccion.getId(), fuenteId)
    );
  }


}

