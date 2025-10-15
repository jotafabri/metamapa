package ar.edu.utn.frba.dds.metamapa.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa.models.dtos.input.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import ar.edu.utn.frba.dds.metamapa.services.IColeccionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  private static final Logger log = LoggerFactory.getLogger(ColeccionesController.class);

  @Autowired
  private IAgregacionService agregacionService;

  @Autowired
  private IColeccionService coleccionService;

  @GetMapping
  public ResponseEntity<List<ColeccionDTO>> getAllColecciones() {
    log.info("Obteniendo todas las colecciones");
    List<ColeccionDTO> colecciones = coleccionService.getAllColecciones();
    return ResponseEntity.ok(colecciones);
  }

  @PostMapping
  public ResponseEntity<ColeccionDTO> crearColeccion(@RequestBody ColeccionDTO coleccionDTO) {
    try {
      log.info("Creando la coleccion {}", coleccionDTO);
      ColeccionDTO coleccionCreada = coleccionService.crearDesdeDTO(coleccionDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(coleccionCreada);
    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{handle}")
  public ResponseEntity<ColeccionDTO> mostrarColeccion(@PathVariable String handle) {
    try {
      log.info("Obteniendo la coleccion con handle {}", handle);

      ColeccionDTO coleccion = coleccionService.mostrarColeccion(handle).orElse(null);
      if (coleccion == null) {
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(coleccion);
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.notFound().build();
    }
  }

  @PatchMapping("/{handle}")
  public ResponseEntity<ColeccionDTO> actualizarColeccion(@PathVariable String handle, @RequestBody ColeccionDTO coleccionDTO) {
    try {
      log.info("Actualizando la coleccion {}", handle);
      ColeccionDTO coleccionActualizada = coleccionService.actualizarColeccion(handle, coleccionDTO);
      return ResponseEntity.ok(coleccionActualizada);
    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{handle}")
  public ResponseEntity<Void> eliminarColeccion(@PathVariable String handle) {
    try {
      log.info("Eliminando la coleccion {}", handle);
      coleccionService.eliminarColeccion(handle);
      return ResponseEntity.noContent().build();
    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  //localhost:8080/
  @PostMapping("/{handle}/fuentes")
  public void agregarFuenteAColeccion(@PathVariable String handle, @RequestParam Long idFuente) {
    this.agregacionService.agregarFuenteAColeccion(handle, idFuente);
  }

  @DeleteMapping("/{handle}/fuentes/{idFuente}")
  public void eliminarFuenteDeColeccion(@PathVariable String handle, @PathVariable Long idFuente) {
    this.agregacionService.eliminarFuenteDeColeccion(handle, idFuente);
  }

  // localhost:8080/colecciones/AccidentesDeTransito/hechos?=...
  @GetMapping("/{handle}/hechos")
  public ResponseEntity<List<HechoDTO>> getHechosByHandle(@PathVariable String handle, @ModelAttribute HechoFiltroDTO filtros) {
    try {
      List<HechoDTO> todosLosHechos = coleccionService.getHechosByHandle(handle, filtros);
      List<HechoDTO> hechosPaginados = paginarHechos(todosLosHechos, filtros.getPage(), filtros.getSize());
      return ResponseEntity.ok(hechosPaginados);
    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{handle}/categorias")
  public ResponseEntity<List<String>> getCategoriasByHandle(@PathVariable String handle) {
    try {
      List<String> categorias = coleccionService.getCategoriasByHandle(handle);
      return ResponseEntity.ok(categorias);
    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  // localhost:8080/colecciones/admin/{handle}/hechos
  @GetMapping("/admin/{handle}/hechos")
  public ResponseEntity<List<HechoDTO>> getHechosByHandleAdmin(@PathVariable String handle, @ModelAttribute HechoFiltroDTO filtros) {
    try {
      List<HechoDTO> todosLosHechos = coleccionService.getHechosByHandleAdmin(handle, filtros);
      List<HechoDTO> hechosPaginados = paginarHechos(todosLosHechos, filtros.getPage(), filtros.getSize());
      return ResponseEntity.ok(hechosPaginados);
    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/refrescar")
  public void refrescarColeccion() {
    agregacionService.refrescarColecciones();
  }

  private List<HechoDTO> paginarHechos(List<HechoDTO> lista, Integer page, Integer size) {
    int start = Math.max(0, page * size);
    int end = Math.min(start + size, lista.size());

    if (start >= lista.size()) {
      return List.of();
    }

    return lista.subList(start, end);
  }
}
