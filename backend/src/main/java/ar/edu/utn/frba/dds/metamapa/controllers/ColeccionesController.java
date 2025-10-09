package ar.edu.utn.frba.dds.metamapa.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import ar.edu.utn.frba.dds.metamapa.services.IColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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

  //localhost:8080/
  @PostMapping("/{handle}/fuentes")
  public void agregarFuenteAColeccion(
      @PathVariable String handle,
      @RequestParam Long idFuente) {
    this.agregacionService.agregarFuenteAColeccion(handle, idFuente);
  }

  @DeleteMapping("/{handle}/fuentes/{idFuente}")
  public void eliminarFuenteDeColeccion(@PathVariable String handle, @PathVariable Long idFuente) {
    this.agregacionService.eliminarFuenteDeColeccion(handle, idFuente);
  }

  // localhost:8080/colecciones/AccidentesDeTransito/hechos?=...
  @GetMapping("/{handle}/hechos")
  public List<HechoDTO> getHechosByHandle(
      @PathVariable String handle,
      @ModelAttribute HechoFiltroDTO filtros
  ) {
    List<HechoDTO> todosLosHechos = coleccionService.getHechosByHandle(
        handle,
        filtros
    );

    // Aplicar paginación manualmente
    int start = filtros.getPage() * filtros.getSize();
    int end = Math.min(start + filtros.getSize(), todosLosHechos.size());

    if (start >= todosLosHechos.size()) {
      return List.of();
    }

    return todosLosHechos.subList(start, end);
  }

  // localhost:8080/colecciones/admin/{handle}/hechos
  @GetMapping("/admin/{handle}/hechos")
  public List<HechoDTO> getHechosByHandleAdmin(
      @PathVariable String handle,
      @ModelAttribute HechoFiltroDTO filtros
  ) {
    return coleccionService.getHechosByHandleAdmin(
        handle,
        filtros
    );
  }

    @GetMapping("/refrescar")
    public void refrescarColeccion() {
        this.agregacionService.refrescarColecciones();
    }

}
