package ar.edu.utn.frba.dds.metamapa.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.SolicitudEliminacionOutputDTO;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudesController {
  @Autowired
  private IAgregacionService agregacionService;

  @PostMapping
  public void crearSolicitud(@RequestBody SolicitudEliminacionInputDTO solicitud) {
    this.agregacionService.crearSolicitud(solicitud);
  }

  @GetMapping
  public List<SolicitudEliminacionOutputDTO> getSolicitudes() {
    return this.agregacionService.findAllSolicitudes();
  }

  @PatchMapping("{id}/aceptar")
  public void aceptarSolicitud(@PathVariable Long id) {
    this.agregacionService.aprobarSolicitudById(id);
  }

  @PatchMapping("{id}/rechazar")
  public void rechazarSolicitud(@PathVariable Long id) {
    this.agregacionService.rechazarSolicitudById(id);
  }
}

