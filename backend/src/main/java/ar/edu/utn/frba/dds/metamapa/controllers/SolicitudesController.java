package ar.edu.utn.frba.dds.metamapa.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.metamapa.ratelimit.RateLimited;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solicitudes")
@RateLimited(maxRequests = 20, durationSeconds = 60)
public class SolicitudesController {
  @Autowired
  private IAgregacionService agregacionService;

  @MutationMapping
  public SolicitudEliminacionOutputDTO crearSolicitud(
          @Argument SolicitudEliminacionInput solicitud) {

    return agregacionService.crearSolicitud(
            solicitud.idHecho(),
            solicitud.razon()
    );
  }



  @QueryMapping
  @PreAuthorize("hasRole('ADMIN')")
  public List<SolicitudEliminacionOutputDTO> getSolicitudes() {
      return agregacionService.findAllSolicitudes();
  }

  @MutationMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> aceptarSolicitud(@Argument SolicitudEliminacionOutput solicitud) {
    try {
      agregacionService.aprobarSolicitudById(solicitud.id());
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @MutationMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> rechazarSolicitud(@Argument SolicitudEliminacionOutput solicitud) {
    try {
      agregacionService.rechazarSolicitudById(solicitud.id());
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  record SolicitudEliminacionInput(Long idHecho, String razon) {
  }

  record SolicitudEliminacionOutput(Long id) {
  }
}

