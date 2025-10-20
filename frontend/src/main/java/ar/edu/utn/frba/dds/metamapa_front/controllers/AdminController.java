package ar.edu.utn.frba.dds.metamapa_front.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa_front.dtos.LoginRequest;
import ar.edu.utn.frba.dds.metamapa_front.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa_front.services.HechosService;
import ar.edu.utn.frba.dds.metamapa_front.services.SolicitudesService;
import ar.edu.utn.frba.dds.metamapa_front.services.UsuarioService;
import ar.edu.utn.frba.dds.metamapa_front.services.ColeccionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        model.addAttribute("titulo", "Panel de administración");
        return "admin/dashboard"; // Template: src/main/resources/templates/admin/dashboard.html
    }

  @GetMapping("/colecciones")
  public String mostrarColecciones(Model model) {
    model.addAttribute("colecciones", coleccionService.getAllColecciones());
    model.addAttribute("titulo", "Administrar colecciones");
    return "admin/colecciones";
  }
  // TODO: Operaciones CRUD sobre las colecciones

  @GetMapping("/hechos")
  public String mostrarHechos(Model model) {
    model.addAttribute("titulo", "Hechos pendientes");
    return "admin/hechos";
  }
  // TODO: POST aprobar hecho, rechazar hecho, modificar y luego aprobar
  // TODO: POST importar archivo CSV

  @PostMapping("/hechos/{id}/aprobar")
  public String aprobarHecho(@PathVariable Long id, Model model) {
    try {
      hechosService.aprobarHecho(id);
      return "redirect:/admin/hechos";
    } catch (Exception e) {
      log.error("Error al aprobar hecho", e);
      model.addAttribute("titulo", "Hechos pendientes");
      return "/admin/hechos";
    }
  }

  @PostMapping("/hechos/{id}/aprobar")
  public String rechazarHecho(@PathVariable Long id, Model model) {
    try {
      hechosService.rechazarHecho(id);
      return "redirect:/admin/hechos";
    } catch (Exception e) {
      log.error("Error al rechazar hecho", e);
      model.addAttribute("titulo", "Hechos pendientes");
      return "/admin/hechos";
    }
  }

  @GetMapping("/solicitudes")
  public String mostrarSolicitudes(Model model) {
    List<SolicitudEliminacionDTO> solicitudes = solicitudesService.obtenerSolicitudes();
    model.addAttribute("titulo", "Solicitudes de eliminación");
    model.addAttribute("listaSolicitudes", solicitudes);
    return "admin/solicitudes";
  }
  // TODO: POST aceptar solicitud (/solicitudes/{id}/aceptar) y rechazar solicitud

  @PostMapping("/solicitudes/{id}/aceptar")
  public String aceptarSolicitud(@PathVariable Long id, Model model) {
    try {
      solicitudesService.aceptarSolicitud(id);
      return "redirect:/admin/solicitudes";
    } catch (Exception e) {
      log.error("Error al aceptar solicitud", e);
      model.addAttribute("titulo", "Solicitudes de eliminación");
      return "/admin/solicitudes";
    }
  }

  @PostMapping("/solicitudes/{id}/rechazar")
  public String rechazarSolicitud(@PathVariable Long id, Model model) {
    try {
      solicitudesService.rechazarSolicitud(id);
      return "redirect:/admin/solicitudes";
    } catch (Exception e) {
      log.error("Error al rechazar solicitud", e);
      model.addAttribute("titulo", "Solicitudes de eliminación");
      return "/admin/solicitudes";
    }
  }

}
