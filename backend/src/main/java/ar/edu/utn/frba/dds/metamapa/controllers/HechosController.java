package ar.edu.utn.frba.dds.metamapa.controllers;

import java.io.IOException;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.exceptions.FechaInvalidaException;
import ar.edu.utn.frba.dds.metamapa.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa.models.dtos.input.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ErrorDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.services.IFileStorageService;
import ar.edu.utn.frba.dds.metamapa.services.IHechosService;
import ar.edu.utn.frba.dds.metamapa.services.ISeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/hechos")
public class HechosController {
  @Autowired
  private IHechosService hechosService;

  @Autowired
  private ISeederService seederService;

  @Autowired
  private IFileStorageService fileStorageService;

  @GetMapping("/me")
  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  public ResponseEntity<List<HechoDTO>> getMisHechos() {
    try {
      String emailUsuario = null;
      var authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
        emailUsuario = authentication.getName();
      }
      List<HechoDTO> hechos = hechosService.getHechosByEmail(emailUsuario);
      return ResponseEntity.ok(hechos);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping
  public ResponseEntity<List<HechoDTO>> getHechosWithParams(
      @ModelAttribute HechoFiltroDTO filtros
  ) {
    try {
      List<HechoDTO> hechos = this.hechosService.getHechosWithParams(filtros);
      return ResponseEntity.ok(hechos);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> crearHechoSinArchivos(
          @RequestBody HechoDTO hechoDTO
  ) {
    try {
      String emailUsuario = null;
      var authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.isAuthenticated()
              && !"anonymousUser".equals(authentication.getPrincipal())) {
        emailUsuario = authentication.getName();
      }

      HechoDTO hechoCreado = hechosService.crearHechoDesdeDTO(hechoDTO, emailUsuario);
      return ResponseEntity.status(HttpStatus.CREATED).body(hechoCreado);

    } catch (FechaInvalidaException e) {
      return ResponseEntity
              .badRequest()
              .body(new ErrorDTO(e.getMessage()));


    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }


  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> crearHecho(
      @RequestPart("hecho") HechoDTO hechoDTO,
      @RequestPart(value = "archivos", required = false) List<MultipartFile> archivos
  ) {
    try {
      // Extraer email del usuario logeado si existe
      String emailUsuario = null;
      var authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
        emailUsuario = authentication.getName();
      }

      if (archivos != null && !archivos.isEmpty()) {
        List<String> nombresGuardados = fileStorageService.guardarMultiples(archivos);
        hechoDTO.setMultimedia(nombresGuardados);
      }

      HechoDTO hechoCreado = hechosService.crearHechoDesdeDTO(hechoDTO, emailUsuario);
      return ResponseEntity.status(HttpStatus.CREATED).body(hechoCreado);

    }catch (FechaInvalidaException e) {
        return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
    } catch (IOException e) {
      return ResponseEntity.badRequest().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<HechoDTO> getHechoById(@PathVariable Long id) {
    try {
      HechoDTO hecho = this.hechosService.getHechoById(id);
      if (hecho == null) {
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(hecho);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  public ResponseEntity<HechoDTO> actualizarHecho(@PathVariable Long id, @RequestBody HechoDTO hechoDTO) {
    try {
      HechoDTO hechoActualizado = this.hechosService.actualizarHecho(id, hechoDTO);
      return ResponseEntity.ok(hechoActualizado);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PatchMapping("/{id}/eliminar")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> marcarEliminado(@PathVariable Long id) {
    try {
      this.hechosService.marcarEliminado(id);
      return ResponseEntity.noContent().build();
    } catch (NotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PatchMapping("/{id}/aprobar")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<HechoDTO> aprobarHecho(@PathVariable Long id, @RequestBody HechoDTO hechoActualizado) {
    try {
      HechoDTO hecho = hechosService.aprobarHecho(id, hechoActualizado);
      return ResponseEntity.ok(hecho);
    } catch (NotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (IllegalStateException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PatchMapping("/{id}/rechazar")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<HechoDTO> rechazarHecho(@PathVariable Long id) {
    try {
      HechoDTO hecho = hechosService.rechazarHecho(id);
      return ResponseEntity.ok(hecho);
    } catch (NotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (IllegalStateException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/inicializar")
  public ResponseEntity<Object> inicializarDatos() {
    try {
      this.seederService.init();
      //this.seederServiceDinamicas.initDinamicas();
      return ResponseEntity.ok(java.util.Map.of("mensaje", "Datos inicializados correctamente"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/pendientes")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<HechoDTO>> obtenerHechosPendientes() {
    try {
      List<HechoDTO> hechos = hechosService.obtenerHechosPendientes();
      return ResponseEntity.ok(hechos);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }
}