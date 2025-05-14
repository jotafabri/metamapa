package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.*;
import ar.edu.utn.frba.dds.metamapa.models.repositories.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.impl.SolicitudesEliminacionRepository;

import ar.edu.utn.frba.dds.metamapa.services.impl.SolicitudesEliminacionService;
import ar.edu.utn.frba.dds.metamapa.services.ISolicitudesEliminacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SolicitudesEliminacionService implements ISolicitudesEliminacionService {

  private SolicitudesEliminacionRepository solicitudesRepository;

  @Override
  public void crear_solicitudDTO(SolicitudEliminacionDTO solicitudDTO){
    solicitudesRepository.save(new SolicitudEliminacion(solicitudDTO.getHecho(), solicitudDTO.getCausa()));
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
