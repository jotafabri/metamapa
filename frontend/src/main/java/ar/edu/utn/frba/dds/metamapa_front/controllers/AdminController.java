package ar.edu.utn.frba.dds.metamapa_front.controllers;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa_front.dtos.*;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa_front.services.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private static final Logger log = LoggerFactory.getLogger(AdminController.class);
  private final UsuarioService usuarioService;
  private final ColeccionService coleccionService;
  private final SolicitudesService solicitudesService;
  private final HechosService hechosService;
  private final FuenteService fuenteService;

  // --- LOGIN ADMIN ---

  @GetMapping("/login")
  public String mostrarLogin(Model model) {
    model.addAttribute("titulo", "Iniciar sesión como administrador");
    model.addAttribute("usuario", new LoginRequest());
    return "admin/login"; // Template: src/main/resources/templates/admin/login.html
  }

  @PostMapping("/login")
  public String procesarLogin(@ModelAttribute("usuario") LoginRequest usuarioDTO, Model model) {
    try {
      var authResponse = usuarioService.autenticar(usuarioDTO);

      if (authResponse != null) {
        return "redirect:/admin";
      } else {
        model.addAttribute("error", "Credenciales inválidas o sin permisos de administrador.");
        model.addAttribute("titulo", "Iniciar sesión como administrador");
        model.addAttribute("usuario", new LoginRequest());
        return "admin/login";
      }

    } catch (Exception e) {
      log.error("Error al iniciar sesión como admin", e);
      model.addAttribute("titulo", "Iniciar sesión como administrador");
      model.addAttribute("usuario", new LoginRequest());
      model.addAttribute("error", "Ocurrió un error al procesar el inicio de sesión.");
      return "admin/login";
    }
  }

  // --- DASHBOARD ---

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public String mostrarDashboard(Model model) {
    model.addAttribute("titulo", "Panel de administración");

    // Cargar estadísticas para el dashboard
    List<HechoDTO> hechosPendientes = hechosService.obtenerHechosPendientes();
    List<ColeccionDTO> colecciones = coleccionService.getAllColecciones();
    List<SolicitudEliminacionDTO> solicitudes = solicitudesService.obtenerSolicitudes();
    List<FuenteDTO> fuentes = fuenteService.listarFuentes();

    log.info("Fuentes obtenidas: {}", fuentes.size());
    if (!fuentes.isEmpty()) {
      log.info("Primera fuente - Tipo: {}, Ruta: {}", fuentes.get(0).getTipo(), fuentes.get(0).getRuta());
    }

    // Pasar datos al modelo
    model.addAttribute("hechosPendientes", hechosPendientes);
    model.addAttribute("totalHechosPendientes", hechosPendientes.size());
    model.addAttribute("colecciones", colecciones);
    model.addAttribute("totalColecciones", colecciones.size());
    model.addAttribute("solicitudes", solicitudes);
    model.addAttribute("totalSolicitudes", solicitudes.size());
    model.addAttribute("fuentes", fuentes);
    model.addAttribute("adminPanel", true);

    return "admin/dashboard"; // Template: src/main/resources/templates/admin/dashboard.html
  }

  @GetMapping("/panel")
  @PreAuthorize("hasRole('ADMIN')")
  public String mostrarPanelCompleto(Model model) {
    model.addAttribute("titulo", "Panel de Administración");
    return "admin/panel"; // Template: src/main/resources/templates/admin/panel.html
  }

  @GetMapping("/colecciones")
  @PreAuthorize("hasRole('ADMIN')")
  public String mostrarColecciones(Model model) {
    model.addAttribute("colecciones", coleccionService.getAllColecciones());
    model.addAttribute("coleccion", new ColeccionDTO());
    model.addAttribute("titulo", "Administrar colecciones");
    return "admin/colecciones";
  }

  @GetMapping("/colecciones/crear")
  @PreAuthorize("hasRole('ADMIN')")
  public String mostrarFormularioCrear(Model model) {
    List<FuenteOutputDTO> todasLasFuentes = fuenteService.obtenerTodasLasFuentes();
    model.addAttribute("todasLasFuentes", todasLasFuentes);
    model.addAttribute("coleccion", new ColeccionDTO());
    model.addAttribute("titulo", "Crear nueva colección");
    model.addAttribute("adminPanel", true);

    return "admin/colecciones/crear";
  }

  @PostMapping("/colecciones/crear")
  @PreAuthorize("hasRole('ADMIN')")
  public String crearColeccion(@ModelAttribute("coleccion") ColeccionDTO coleccionDTO,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
    try {
      convertirFuentesIdsAFuentes(coleccionDTO);
      ColeccionDTO coleccionCreada = coleccionService.crearColeccion(coleccionDTO);
      redirectAttributes.addFlashAttribute("toastMessage", "Colección creada con éxito ✅");
      redirectAttributes.addFlashAttribute("toastType", "success");
      return "redirect:/admin";
    } catch (Exception e) {
      log.error("Error al crear nueva colección", e);

      // Volver a cargar los datos necesarios para el formulario
      List<FuenteOutputDTO> todasLasFuentes = fuenteService.obtenerTodasLasFuentes();
      model.addAttribute("todasLasFuentes", todasLasFuentes);
      model.addAttribute("titulo", "Crear colección");
      model.addAttribute("adminPanel", true);
      model.addAttribute("toastMessage", "Error al crear colección: " + e.getMessage());
      model.addAttribute("toastType", "error");

      return "admin/colecciones/crear";
    }
  }

  @GetMapping("/colecciones/{handle}/editar")
  @PreAuthorize("hasRole('ADMIN')")
  public String mostrarFormularioEditar(
      @PathVariable String handle,
      Model model) {
    try {
      ColeccionDTO coleccionDTO = coleccionService.getColeccionByHandle(handle).get();

      List<FuenteOutputDTO> todasLasFuentes = fuenteService.obtenerTodasLasFuentes();

      List<Long> fuentesIds = coleccionDTO.getFuentes() != null
              ? coleccionDTO.getFuentes().stream().map(FuenteOutputDTO::getId).toList()
              : new ArrayList<>();
      coleccionDTO.setFuentesIds(fuentesIds);

      model.addAttribute("coleccion", coleccionDTO);
      model.addAttribute("todasLasFuentes", todasLasFuentes);

      model.addAttribute("titulo", "Editar colección");
      model.addAttribute("adminPanel", true);
      return "admin/colecciones/editar";
    } catch (NotFoundException e) {
      return "redirect:/404";
    }
  }

  @PostMapping("/colecciones/{handle}/actualizar")
  @PreAuthorize("hasRole('ADMIN')")
  public String actualizarColeccion(@PathVariable String handle,
                                    @ModelAttribute("coleccion") ColeccionDTO coleccionDTO,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
    try {
      convertirFuentesIdsAFuentes(coleccionDTO);
      ColeccionDTO coleccionActualizada = coleccionService.actualizarColeccion(handle, coleccionDTO);
      redirectAttributes.addFlashAttribute("toastMessage", "Colección actualizada con éxito ✅");
      redirectAttributes.addFlashAttribute("toastType", "success");
      return "redirect:/admin";
    } catch (NotFoundException e) {
      redirectAttributes.addFlashAttribute("toastMessage", "Colección no encontrada ❌");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/404";
    } catch (Exception e) {
      log.error("Error al editar colección {}", handle, e);

      // Volver a cargar los datos necesarios para el formulario
      List<FuenteOutputDTO> todasLasFuentes = fuenteService.obtenerTodasLasFuentes();
      model.addAttribute("todasLasFuentes", todasLasFuentes);
      model.addAttribute("coleccion", coleccionDTO);
      model.addAttribute("titulo", "Editar colección");
      model.addAttribute("adminPanel", true);
      model.addAttribute("toastMessage", "Error al actualizar colección: " + e.getMessage());
      model.addAttribute("toastType", "error");

      return "admin/colecciones/editar";
    }
  }

  @PostMapping("/colecciones/{handle}/eliminar")
  @PreAuthorize("hasRole('ADMIN')")
  public String eliminarColeccion(@PathVariable String handle ,RedirectAttributes redirectAttributes) {
    try {
      coleccionService.eliminarColeccion(handle);
      redirectAttributes.addFlashAttribute("toastMessage", "Coleccion eliminada con éxito ✅");
      redirectAttributes.addFlashAttribute("toastType", "success");
      return "redirect:/admin";
    } catch (NotFoundException e) {
      redirectAttributes.addFlashAttribute("toastMessage", "Colección no encontrada ❌");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/404";
    } catch (Exception e) {
      log.error("Error al eliminar colección {}", handle, e);
      redirectAttributes.addFlashAttribute("toastMessage", "Ocurrió un error al eliminar la colección ⚠️");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/admin";
    }
  }

  @GetMapping("/hechos")
  @PreAuthorize("hasRole('ADMIN')")
  public String mostrarHechos(Model model) {
    List<HechoDTO> hechosPendientes = hechosService.obtenerHechosPendientes();
    model.addAttribute("hechosPendientes", hechosPendientes);
    model.addAttribute("titulo", "Hechos pendientes");
    model.addAttribute("adminPanel", true);
    return "admin/moderacion";
  }
  // TODO: POST importar archivo CSV

  @PostMapping("/hechos/{id}/aprobar")
  @PreAuthorize("hasRole('ADMIN')")
  public String aprobarHecho(@PathVariable Long id,
                             Model model,
                             @ModelAttribute("hechoActualizado") HechoDTO hechoActualizado,
                             RedirectAttributes redirectAttributes) {
    try {
      hechosService.aprobarHecho(id, hechoActualizado);
      redirectAttributes.addFlashAttribute("toastMessage", "Hecho aprobado con éxito ✅");
      redirectAttributes.addFlashAttribute("toastType", "success");
      return "redirect:/admin";
    } catch (NotFoundException e) {
      redirectAttributes.addFlashAttribute("toastMessage", "Hecho no encontrado ❌");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/404";
    } catch (Exception e) {
      log.error("Error al aprobar hecho {}", id, e);
      redirectAttributes.addFlashAttribute("toastMessage", "Ocurrió un error al aprobar el hecho ⚠️");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/admin";
    }
  }

  @PostMapping("/hechos/{id}/rechazar")
  @PreAuthorize("hasRole('ADMIN')")
  public String rechazarHecho(@PathVariable Long id,
                              Model model,
                              RedirectAttributes redirectAttributes) {
    try {
      hechosService.rechazarHecho(id);
      redirectAttributes.addFlashAttribute("toastMessage", "Hecho rechazado con éxito 🚫");
      redirectAttributes.addFlashAttribute("toastType", "success");
      return "redirect:/admin";
    } catch (NotFoundException e) {
      redirectAttributes.addFlashAttribute("toastMessage", "Hecho no encontrado ❌");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/404";
    } catch (Exception e) {
      log.error("Error al rechazar hecho {}", id, e);
      redirectAttributes.addFlashAttribute("toastMessage", "Ocurrió un error al rechazar el hecho ⚠️");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/admin";
    }
  }


  @GetMapping("/solicitudes")
  @PreAuthorize("hasRole('ADMIN')")
  public String mostrarSolicitudes(Model model) {
    List<SolicitudEliminacionDTO> solicitudes = solicitudesService.obtenerSolicitudes();
    model.addAttribute("titulo", "Solicitudes de eliminación");
    model.addAttribute("listaSolicitudes", solicitudes);
    model.addAttribute("adminPanel", true);
    return "admin/solicitudes";
  }

  @PostMapping("/solicitudes/{id}/aceptar")
  @PreAuthorize("hasRole('ADMIN')")
  public String aceptarSolicitud(@PathVariable Long id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
    try {
      solicitudesService.aceptarSolicitud(id);
      redirectAttributes.addFlashAttribute("toastMessage", "Solicitud aceptada con éxito ✅");
      redirectAttributes.addFlashAttribute("toastType", "success");
      return "redirect:/admin";
    } catch (NotFoundException e) {
      redirectAttributes.addFlashAttribute("toastMessage", "Solicitud no encontrada ❌");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/404";
    } catch (Exception e) {
      log.error("Error al aceptar solicitud {}", id, e);
      redirectAttributes.addFlashAttribute("toastMessage", "Ocurrió un error al aceptar la solicitud ⚠️");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/admin";
    }
  }

  @PostMapping("/solicitudes/{id}/rechazar")
  @PreAuthorize("hasRole('ADMIN')")
  public String rechazarSolicitud(@PathVariable Long id,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
    try {
      solicitudesService.rechazarSolicitud(id);
      redirectAttributes.addFlashAttribute("toastMessage", "Solicitud rechazada con éxito 🚫");
      redirectAttributes.addFlashAttribute("toastType", "success");
      return "redirect:/admin";
    } catch (NotFoundException e) {
      redirectAttributes.addFlashAttribute("toastMessage", "Solicitud no encontrada ❌");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/404";
    } catch (Exception e) {
      log.error("Error al rechazar solicitud {}", id, e);
      redirectAttributes.addFlashAttribute("toastMessage", "Ocurrió un error al rechazar la solicitud ⚠️");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/admin";
    }
  }

  private void convertirFuentesIdsAFuentes(ColeccionDTO coleccionDTO) {
    if (coleccionDTO.getFuentesIds() != null && !coleccionDTO.getFuentesIds().isEmpty()) {
      List<FuenteOutputDTO> todasLasFuentes = fuenteService.obtenerTodasLasFuentes();
      List<FuenteOutputDTO> fuentesSeleccionadas = todasLasFuentes.stream()
          .filter(fuente -> coleccionDTO.getFuentesIds().contains(fuente.getId()))
          .toList();
      coleccionDTO.setFuentes(fuentesSeleccionadas);
    }
  }

  @PostMapping("/fuentes/crear")
  @PreAuthorize("hasRole('ADMIN')")
  public String crearFuente(@ModelAttribute FuenteDTO fuenteDTO,
                            RedirectAttributes redirectAttributes) {
    try {
      fuenteService.crearFuente(fuenteDTO);
      redirectAttributes.addFlashAttribute("toastMessage", "Fuente creada con éxito ✅");
      redirectAttributes.addFlashAttribute("toastType", "success");
      return "redirect:/admin";
    } catch (IllegalArgumentException e) {
      log.error("Error al crear fuente: {}", e.getMessage());
      redirectAttributes.addFlashAttribute("toastMessage", "Datos de fuente inválidos ❌");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/admin";
    } catch (Exception e) {
      log.error("Error al crear fuente", e);
      redirectAttributes.addFlashAttribute("toastMessage", "Ocurrió un error al crear la fuente ⚠️");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/admin";
    }
  }

  @PostMapping("/fuentes/crear-estatica")
  @PreAuthorize("hasRole('ADMIN')")
  public String crearFuenteEstatica(@RequestParam("archivo") org.springframework.web.multipart.MultipartFile archivo,
                                    @RequestParam(value = "titulo", required = false) String titulo,
                                    RedirectAttributes redirectAttributes) {
    try {
      fuenteService.crearFuenteEstatica(archivo, titulo);
      redirectAttributes.addFlashAttribute("toastMessage", "Fuente estática creada con éxito ✅");
      redirectAttributes.addFlashAttribute("toastType", "success");
      return "redirect:/admin";
    } catch (IllegalArgumentException e) {
      log.error("Error al crear fuente estática: {}", e.getMessage());
      redirectAttributes.addFlashAttribute("toastMessage", "Archivo CSV inválido ❌");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/admin";
    } catch (Exception e) {
      log.error("Error al crear fuente estática", e);
      redirectAttributes.addFlashAttribute("toastMessage", "Ocurrió un error al subir el archivo ⚠️");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/admin";
    }
  }

}
