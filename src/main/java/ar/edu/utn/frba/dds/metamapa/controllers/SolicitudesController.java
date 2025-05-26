package ar.edu.utn.frba.dds.metamapa.controllers;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudesController {
  @Autowired
  private IAgregacionService agregacionService;

  @PostMapping
  public void crearSolicitud(@RequestBody SolicitudEliminacionDTO solicitud) {
    this.agregacionService.crearSolicitud(solicitud);
  }

  @GetMapping
  public List<SolicitudEliminacionDTO> getSolicitudes(){
    return this.agregacionService.findAllSolicitudes();
  }

  /*@PatchMapping("/solicitudes/{id}")  //falta verificar que sea Admin o no se que
  public String tomarDecision(@PathVariable long id, @RequestBody SolicitudEliminacionDTO solicitud)) {
    agregacionService.procesarDecision(id, decision);  //que Service deberia procesar la decision?
    return "Decisión procesada para solicitud " + id;
  }*/
}