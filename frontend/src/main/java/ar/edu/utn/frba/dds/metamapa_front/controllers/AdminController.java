package ar.edu.utn.frba.dds.metamapa_front.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa_front.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.LoginRequest;
import ar.edu.utn.frba.dds.metamapa_front.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa_front.services.ColeccionService;
import ar.edu.utn.frba.dds.metamapa_front.services.HechosService;
import ar.edu.utn.frba.dds.metamapa_front.services.SolicitudesService;
import ar.edu.utn.frba.dds.metamapa_front.services.UsuarioService;
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
        return "redirect:/admin/dashboard";
      } else {
        model.addAttribute("error", "Credenciales inválidas o sin permisos de administrador.");
        return "admin/login";
      }

    } catch (Exception e) {
      log.error("Error al iniciar sesión como admin", e);
      model.addAttribute("titulo", "Iniciar sesión como administrador");
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

    // Pasar datos al modelo
    model.addAttribute("hechosPendientes", hechosPendientes);
    model.addAttribute("totalHechosPendientes", hechosPendientes.size());
    model.addAttribute("colecciones", colecciones);
    model.addAttribute("totalColecciones", colecciones.size());
    model.addAttribute("solicitudes", solicitudes);
    model.addAttribute("totalSolicitudes", solicitudes.size());

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
    model.addAttribute("coleccion", new ColeccionDTO());
    model.addAttribute("titulo", "Crear nueva colección");
    return "admin/colecciones/crear";
  }

  @PostMapping("/colecciones/crear")
  @PreAuthorize("hasRole('ADMIN')")
  public String crearColeccion(@ModelAttribute("coleccion") ColeccionDTO coleccionDTO,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
    try {
      ColeccionDTO coleccionCreada = coleccionService.crearColeccion(coleccionDTO);
      return "redirect:/admin";
    } catch (Exception e) {
      log.error("Error al crear nueva colección", e);
      model.addAttribute("titulo", "Crear colección");
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

      model.addAttribute("coleccion", coleccionDTO);
      model.addAttribute("titulo", "Editar colección");
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
      ColeccionDTO coleccionActualizada = coleccionService.actualizarColeccion(handle, coleccionDTO);

      return "redirect:/admin";
    } catch (NotFoundException e) {
      return "redirect:/404";
    } catch (Exception e) {
      log.error("Error al editar colección {}", handle, e);
      model.addAttribute("titulo", "Editar colección");
      return "admin/colecciones/editar";
    }
  }

  @PostMapping("/colecciones/{handle}/eliminar")
  @PreAuthorize("hasRole('ADMIN')")
  public String eliminarColeccion(@PathVariable String handle) {
    try {
      coleccionService.eliminarColeccion(handle);
      return "redirect:/admin";
    } catch (NotFoundException e) {
      return "redirect:/404";
    } catch (Exception e) {
      log.error("Error al eliminar colección {}", handle, e);
      return "redirect:/admin";
    }
  }

  @GetMapping("/hechos")
  @PreAuthorize("hasRole('ADMIN')")
  public String mostrarHechos(Model model) {
    List<HechoDTO> hechosPendientes = hechosService.obtenerHechosPendientes();
    model.addAttribute("hechosPendientes", hechosPendientes);
    model.addAttribute("titulo", "Hechos pendientes");
    return "admin/moderacion";
  }
  // TODO: POST importar archivo CSV

  @PostMapping("/hechos/{id}/aprobar")
  @PreAuthorize("hasRole('ADMIN')")
  public String aprobarHecho(@PathVariable Long id, Model model, @ModelAttribute("hechoActualizado") HechoDTO hechoActualizado) {
    try {
      hechosService.aprobarHecho(id, hechoActualizado);
      return "redirect:/admin";
    } catch (Exception e) {
      log.error("Error al aprobar hecho", e);
      model.addAttribute("titulo", "Hechos pendientes");
      return "redirect:/admin";
    }
  }

  @PostMapping("/hechos/{id}/rechazar")
  @PreAuthorize("hasRole('ADMIN')")
  public String rechazarHecho(@PathVariable Long id, Model model) {
    try {
      hechosService.rechazarHecho(id);
      return "redirect:/admin";
    } catch (Exception e) {
      log.error("Error al rechazar hecho", e);
      model.addAttribute("titulo", "Hechos pendientes");
      return "redirect:/admin";
    }
  }

  @GetMapping("/solicitudes")
  @PreAuthorize("hasRole('ADMIN')")
  public String mostrarSolicitudes(Model model) {
    List<SolicitudEliminacionDTO> solicitudes = solicitudesService.obtenerSolicitudes();
    model.addAttribute("titulo", "Solicitudes de eliminación");
    model.addAttribute("listaSolicitudes", solicitudes);
    return "admin/solicitudes";
  }

  @PostMapping("/solicitudes/{id}/aceptar")
  @PreAuthorize("hasRole('ADMIN')")
  public String aceptarSolicitud(@PathVariable Long id, Model model) {
    try {
      solicitudesService.aceptarSolicitud(id);
      return "redirect:/admin";
    } catch (Exception e) {
      log.error("Error al aceptar solicitud", e);
      model.addAttribute("titulo", "Solicitudes de eliminación");
      return "redirect:/admin";
    }
  }

  @PostMapping("/solicitudes/{id}/rechazar")
  @PreAuthorize("hasRole('ADMIN')")
  public String rechazarSolicitud(@PathVariable Long id, Model model) {
    try {
      solicitudesService.rechazarSolicitud(id);
      return "redirect:/admin";
    } catch (Exception e) {
      log.error("Error al rechazar solicitud", e);
      model.addAttribute("titulo", "Solicitudes de eliminación");
      return "redirect:/admin";
    }
  }

}
