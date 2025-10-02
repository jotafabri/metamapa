package ar.edu.utn.frba.dds.metamapa_front.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
  private static final Logger log = LoggerFactory.getLogger(AdminController.class);

  @GetMapping
  public String admin() {
    return "redirect:/admin/dashboard";
  }

  @GetMapping("/dashboard")
  @PreAuthorize("hasRole('ADMIN')")
  public String dashboard(Model model, Authentication authentication) {
    model.addAttribute("titulo", "Panel de administración");
    return "admin/dashboard";
  }

  @GetMapping("/hechos")
  public String gestionHechos(Model model) {
    model.addAttribute("titulo", "Gestión de hechos");
    return "admin/hechos";
  }

  @GetMapping("/colecciones")
  public String gestionColecciones(Model model) {
    model.addAttribute("titulo", "Gestión de colecciones");
    return "admin/colecciones";
  }

  @GetMapping("/solicitudes")
  public String gestionSolicitudes(Model model) {
    model.addAttribute("titulo", "Solicitudes de eliminación");
    return "admin/solicitudes";
  }

}
