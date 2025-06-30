package ar.edu.utn.frba.dds.metamapa.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;

import ar.edu.utn.frba.dds.metamapa.models.entities.enums.TipoAlgoritmo;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import ar.edu.utn.frba.dds.metamapa.services.IColeccionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/colecciones")
public class ColeccionesController {
  @Autowired
  private IAgregacionService agregacionService;

  @Autowired
  private IColeccionService coleccionService;

  @GetMapping
  public List<ColeccionDTO> getAllColecciones() {
    return this.coleccionService.getAllColecciones();
  }

  @PostMapping
  public void crearColeccion(@RequestBody ColeccionDTO coleccionDTO) {
    this.coleccionService.crearDesdeDTO(coleccionDTO);
  }

  @GetMapping("/{handle}")
  public ColeccionDTO mostrarColeccion(@PathVariable String handle) {
    return this.coleccionService.mostrarColeccion(handle);
  }

  @PatchMapping("/{handle}")
  public void actualizarColeccion(@PathVariable String handle, @RequestBody ColeccionDTO coleccionDTO) {
    this.coleccionService.actualizarColeccion(handle, coleccionDTO);
  }

  @DeleteMapping("/{handle}")
  public void eliminarColeccion(@PathVariable String handle) {
    this.coleccionService.eliminarColeccion(handle);
  }

  @PutMapping("/{handle}/algoritmo")
  public void modificarAlgoritmo(@PathVariable String handle, @RequestParam String nombre) {
    TipoAlgoritmo tipo = TipoAlgoritmo.valueOf(nombre.toUpperCase());
    this.coleccionService.cambiarAlgoritmo(handle, tipo);
  }

  //localhost:8080/colecciones/abcd/fuentes?idFuente=3
  @PostMapping("/{handle}/fuentes")
  public void agregarFuenteAColeccion(
      @PathVariable String handle,
      @RequestParam Long idFuente) {
    this.agregacionService.agregarFuenteAColeccion(handle, idFuente);
  }

  @DeleteMapping("/{handle}/fuentes/{idFuente}")
  public void eliminarFuenteDeColeccion(@PathVariable String handle, @PathVariable Long idFuente) {
    this.agregacionService.eliminarFuenteDeColeccion(handle,idFuente);
  }

  // localhost:8080/colecciones/AccidentesDeTransito/hechos?=...
  @GetMapping("/{handle}/hechos")
  public List<HechoDTO> getHechosByHandle(
      @PathVariable String handle,
      @RequestParam(required = false) String categoria,
      @RequestParam(required = false) String fecha_reporte_desde,
      @RequestParam(required = false) String fecha_reporte_hasta,
      @RequestParam(required = false) String fecha_acontecimiento_desde,
      @RequestParam(required = false) String fecha_acontecimiento_hasta,
      @RequestParam(required = false) String ubicacion,
      @RequestParam(required = false) Boolean soloConMultimedia,
      @RequestParam(required = false) Boolean soloConContribuyente
  ) {
    return coleccionService.getHechosByHandle(
        handle,
        categoria,
        fecha_reporte_desde,
        fecha_reporte_hasta,
        fecha_acontecimiento_desde,
        fecha_acontecimiento_hasta,
        ubicacion,
        soloConMultimedia,
        soloConContribuyente);
  }

  // localhost:8080/colecciones/AccidentesDeTransito/hechos?curados=true
  @GetMapping(value = "/{handle}/hechos", params = "curados")
  public List<HechoDTO> getHechosCurados(
      @PathVariable String handle,
      @RequestParam Boolean curados
  ) {
    return this.coleccionService.getHechosCurados(handle, curados);
  }
}
