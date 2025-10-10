package ar.edu.utn.frba.dds.metamapa_front.controllers;

import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IFuentesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.metamapa.services.IEstadisticasService;

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

  //private static final Logger log = LoggerFactory.getLogger(AdminController.class);

  private final IEstadisticasService estadisticasService;
  private final IHechosRepository hechosRepository;
  private final IFuentesRepository fuentesRepository;
  private final ISolicitudesEliminacionRepository solicitudesRepository;
  private final IColeccionesRepository coleccionesRepository;

  @GetMapping
  public String admin() {
    return "redirect:/admin/dashboard";
  }

  @GetMapping("/dashboard")
  //@PreAuthorize("hasRole('ADMIN')")
  public String dashboard(Model model/*, Authentication authentication*/) {
    model.addAttribute("titulo", "Panel de administración");
    model.addAttribute("totalHechos", hechosRepository.count());
    model.addAttribute("totalFuentes", fuentesRepository.count());
    model.addAttribute("totalSolicitudes", solicitudesRepository.count());
    model.addAttribute("totalColecciones", coleccionesRepository.count());

    model.addAttribute("categoriaTop", estadisticasService.obtenerCategoriaConMasHechos());
    model.addAttribute("solicitudesSpam", estadisticasService.obtenerCantidadSolicitudesSpam());
    return "admin/dashboard";
  }

  @GetMapping("/dashboard/actualizar")
  public String actualizarEstadisticas() {
    estadisticasService.actualizarEstadisticas();
    return "redirect:/admin/dashboard";
  }
/*
  @GetMapping("/dashboard/exportar")
  public ResponseEntity<Resource> exportarEstadisticasCSV() {
    Resource csv = estadisticasService.exportarEstadisticasCSV();
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estadisticas.csv")
            .body(csv);
  }
*/
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
