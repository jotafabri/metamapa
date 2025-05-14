package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.util.Objects;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.SolicitudEliminacion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.metamapa.services.ISolicitudesEliminacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolicitudesEliminacionService implements ISolicitudesEliminacionService {

  @Autowired
  private ISolicitudesEliminacionRepository solicitudesRepository;

  @Autowired
  private IHechosRepository hechosRepository;

  @Override
  public void crearSolicitud(SolicitudEliminacionDTO solicitudDTO) {
    var hecho = this.hechosRepository.findById(solicitudDTO.getIdHecho());
    if (hecho != null) {
      var solicitud = new SolicitudEliminacion(
          hecho,
          solicitudDTO.getRazon());
      this.solicitudesRepository.agregarSolicitud(solicitud);
    }
  }

  @Override
  public void aprobarSolicitud(SolicitudEliminacion solicitud) {

    Objects.requireNonNull(solicitud, "La solicitud no puede ser nula");

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
    Objects.requireNonNull(solicitud, "La solicitud no puede ser nula");

    if (solicitud.getEstado() != Estado.PENDIENTE) {
      throw new IllegalStateException("Solo se pueden rechazar solicitudes pendientes");
    }

    if (solicitud.getHecho() == null) {
      throw new IllegalStateException("La solicitud no tiene un hecho asociado válido");
    }


    solicitud.rechazarSolicitud();
    solicitudesRepository.save(solicitud);
  }

}
