package ar.edu.utn.frba.dds.metamapa.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hechos")
public class HechosController {
  @Autowired
  private IHechosService hechosService;

  @GetMapping
  public List<HechoDTO> getHechosWithParams(
      @RequestParam(required = false) String categoria,
      @RequestParam(required = false) String fecha_reporte_desde,
      @RequestParam(required = false) String fecha_reporte_hasta,
      @RequestParam(required = false) String fecha_acontecimiento_desde,
      @RequestParam(required = false) String fecha_acontecimiento_hasta,
      @RequestParam(required = false) String ubicacion
  ) {
    return this.hechosService.getHechosWithParams(
        categoria,
        fecha_reporte_desde,
        fecha_reporte_hasta,
        fecha_acontecimiento_desde,
        fecha_acontecimiento_hasta,
        ubicacion);
  }
}
