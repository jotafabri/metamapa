package ar.edu.utn.frba.dds.metamapa.controllers;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.metamapa.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa.models.dtos.input.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.DatosGeograficosDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import ar.edu.utn.frba.dds.metamapa.services.IColeccionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  public ResponseEntity<List<ColeccionDTO>> getAllColecciones(@RequestParam(required = false) Integer limit) {
    log.info("Obteniendo todas las colecciones");
    List<ColeccionDTO> colecciones;
    if (limit != null) {
      colecciones = coleccionService.getAllColecciones(limit);
    } else {
      colecciones = coleccionService.getAllColecciones();
    }
    return ResponseEntity.ok(colecciones);
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
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
  @PreAuthorize("hasRole('ADMIN')")
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
  @PreAuthorize("hasRole('ADMIN')")
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

  // Métodos del servicio agregador, utilizan graphQL
  @MutationMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> agregarFuenteAColeccion(@Argument FuenteInput fuenteInput, @Argument ColeccionInput coleccionInput) {
    try {
      agregacionService.agregarFuenteAColeccion(coleccionInput.handle(), fuenteInput.id());
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @MutationMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> eliminarFuenteDeColeccion(@Argument FuenteInput fuenteInput, @Argument ColeccionInput coleccionInput) {
    try {
      agregacionService.eliminarFuenteDeColeccion(coleccionInput.handle(), fuenteInput.id());
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @MutationMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> reemplazarFuentesColeccion(@Argument ColeccionInput coleccion, @Argument List<FuenteInput> fuentes) {
    String handle = coleccion.handle();
    List<Long> idsFuentesDeseadas = fuentes.stream()
        .map(FuenteInput::id)
        .collect(Collectors.toList());
    try {
      agregacionService.sincronizarFuentesColeccion(handle, idsFuentesDeseadas);
      return ResponseEntity.ok().build();
    } catch (NotFoundException e) {
      log.error(e.getMessage());
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      log.error("Error al reemplazar fuentes de la colección {}: {}", handle, e.getMessage());
      return ResponseEntity.badRequest().build();
    }
  }

  record FuenteInput(Long id) {
  }

  record ColeccionInput(String handle) {
  }

  // localhost:8080/colecciones/AccidentesDeTransito/hechos?=...
  @GetMapping("/{handle}/hechos")
  public ResponseEntity<List<HechoDTO>> getHechosByHandle(
          @PathVariable String handle,
          @ModelAttribute HechoFiltroDTO filtros) {
    try {
      log.info("💻 Request GET /colecciones/{}/hechos recibida con filtros: {}", handle, filtros);

      List<HechoDTO> todosLosHechos = coleccionService.getHechosByHandle(handle, filtros);
      log.info("🌐 Coleccion '{}' devuelve {} hechos antes de paginar", handle, todosLosHechos.size());

      List<HechoDTO> hechosPaginados = paginarHechos(todosLosHechos, filtros.getPage(), filtros.getSize());
      log.info("📄 Hechos paginados (page={}, size={}): {} elementos", filtros.getPage(), filtros.getSize(), hechosPaginados.size());

      return ResponseEntity.ok(hechosPaginados);
    } catch (NotFoundException e) {
      log.error("❌ Coleccion '{}' no encontrada: {}", handle, e.getMessage());
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      log.error("⚠ Error al obtener hechos de la coleccion '{}': {}", handle, e.getMessage(), e);
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{handle}/ubicaciones")
  public ResponseEntity<DatosGeograficosDTO> getDatosGeograficosByHandle(@PathVariable String handle) {
    try {
      log.info("💻 Request GET /colecciones/{}/ubicaciones recibida", handle);

      DatosGeograficosDTO ubicaciones = coleccionService.obtenerDatosGeograficos(handle);
      log.info("🌐 Datos geográficos de '{}': paises={}, provincias={}, localidades={}",
              handle,
              ubicaciones.getPaises().size(),
              ubicaciones.getProvincias().size(),
              ubicaciones.getLocalidades().size());

      return ResponseEntity.ok(ubicaciones);
    } catch (NotFoundException e) {
      log.error("❌ Coleccion '{}' no encontrada: {}", handle, e.getMessage());
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      log.error("⚠ Error al obtener datos geograficos de la coleccion '{}': {}", handle, e.getMessage(), e);
      return ResponseEntity.badRequest().build();
    }
  }


  // localhost:8080/colecciones/admin/{handle}/hechos
  @GetMapping("/admin/{handle}/hechos")
  @PreAuthorize("hasRole('ADMIN')")
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
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Object> refrescarColeccion() {
    try {
      agregacionService.refrescarColecciones();
      return ResponseEntity.ok(java.util.Map.of("mensaje", "Colecciones refrescadas correctamente"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
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
