package ar.edu.utn.frba.dds.metamapa.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.metamapa.ratelimit.RateLimited;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solicitudes")
@RateLimited(maxRequests = 20, durationSeconds = 60)
public class SolicitudesController {
  @Autowired
  private IAgregacionService agregacionService;

  @PostMapping
  public ResponseEntity<SolicitudEliminacionOutputDTO> crearSolicitud(@RequestBody SolicitudEliminacionInputDTO solicitud) {
    try {
      SolicitudEliminacionOutputDTO solicitudCreada = this.agregacionService.crearSolicitud(solicitud);
      return ResponseEntity.status(HttpStatus.CREATED).body(solicitudCreada);
    } catch (NotFoundException e) {
      return ResponseEntity.notFound().build();
    }catch (Exception e) {
        return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<SolicitudEliminacionOutputDTO>> getSolicitudes() {
    try {
      List<SolicitudEliminacionOutputDTO> solicitudes = this.agregacionService.findAllSolicitudes();
      return ResponseEntity.ok(solicitudes);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PatchMapping("{id}/aceptar")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> aceptarSolicitud(@PathVariable Long id) {
    try {
      this.agregacionService.aprobarSolicitudById(id);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PatchMapping("{id}/rechazar")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> rechazarSolicitud(@PathVariable Long id) {
    try {
      this.agregacionService.rechazarSolicitudById(id);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }
}

