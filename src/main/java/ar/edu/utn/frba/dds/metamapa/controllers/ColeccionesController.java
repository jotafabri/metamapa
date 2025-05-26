package ar.edu.utn.frba.dds.metamapa.controllers;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;

import ar.edu.utn.frba.dds.metamapa.services.IColeccionService;
import ar.edu.utn.frba.dds.metamapa.services.impl.AgregacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/colecciones")
public class ColeccionesController {
  @Autowired
  private AgregacionService agregacionService;

  @Autowired
  private IColeccionService coleccionService;

  @GetMapping
  public List<ColeccionDTO> getAllColecciones() {
    return this.coleccionService.getAllColecciones();
  }

  @GetMapping("/{handle}/hechos")
  public List<HechoDTO> getHechosByHandle(
      @PathVariable String handle,
      @RequestParam(required = false) String categoria,
      @RequestParam(required = false) String fecha_reporte_desde,
      @RequestParam(required = false) String fecha_reporte_hasta,
      @RequestParam(required = false) String fecha_acontecimiento_desde,
      @RequestParam(required = false) String fecha_acontecimiento_hasta,
      @RequestParam(required = false) String ubicacion
  ) {
    return coleccionService.getHechosByHandle(
        handle,
        categoria,
        fecha_reporte_desde,
        fecha_reporte_hasta,
        fecha_acontecimiento_desde,
        fecha_acontecimiento_hasta,
        ubicacion);
  }

//localhost:8080/colecciones/agregar-fuente?handleColeccion=abcd&idFuente=3
  @PostMapping("/agregar-fuente")
  public void agregarFuenteAColeccion(@RequestParam String handleColeccion, @RequestParam Long idFuente) {
    this.agregacionService.agregarFuenteAColeccion(handleColeccion, idFuente);
  }

}
