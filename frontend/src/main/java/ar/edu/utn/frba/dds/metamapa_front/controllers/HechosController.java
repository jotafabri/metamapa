package ar.edu.utn.frba.dds.metamapa_front.controllers;

import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa_front.services.HechosService;
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
@RequestMapping("/hechos")
@RequiredArgsConstructor
public class HechosController {
  private static final Logger log = LoggerFactory.getLogger(HechosController.class);
  private final HechosService hechosService;

  @GetMapping("/{id}")
  public String verDetalleHecho(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
    try {
      HechoDTO hecho = hechosService.getHechoById(id).get();

      model.addAttribute("hecho", hecho);
      model.addAttribute("titulo", "Detalle del hecho");

      return "hechos/detalle";
    } catch (NotFoundException e) {
      return "redirect:/404";
    }
  }

  @GetMapping("/nuevo")
  public String mostrarFormularioCrear(Model model) {
    model.addAttribute("hecho", new HechoDTO());
    model.addAttribute("titulo", "Contribuir");
    return "hechos/contribuir";
  }

  @PostMapping("/crear")
  public String crearHecho(@ModelAttribute("hecho") HechoDTO hechoDTO,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {

    try {
      HechoDTO hechoCreado = hechosService.crearHecho(hechoDTO);
      return "redirect:/colecciones/colecciones";
    } catch (Exception e) {
      log.error("Error al crear nuevo hecho", e);
      model.addAttribute("titulo", "Contribuir");
      return "hechos/contribuir";
    }
  }

  @GetMapping("/{id}/editar")
  @PreAuthorize("hasAuthority('EDITAR_HECHOS')")
  public String mostrarFormularioEditar(
      @PathVariable Long id,
      Model model) {
    try {
      HechoDTO hechoDTO = hechosService.getHechoById(id).get();

      model.addAttribute("coleccion", hechoDTO);
      model.addAttribute("titulo", "Editar hecho");
      return "hechos/editar";
    } catch (NotFoundException e) {
      return "redirect:/404";
    }
  }

  @PostMapping("/{id}/actualizar")
  @PreAuthorize("hasAuthority('EDITAR_HECHOS')")
  public String actualizarColeccion(@PathVariable Long id,
                                    @ModelAttribute("hecho") HechoDTO hechoDTO,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
    try {
      HechoDTO hechoActualizado = hechosService.actualizarHecho(id, hechoDTO);

      return "redirect:/colecciones/colecciones";
    } catch (NotFoundException e) {
      return "redirect:/404";
    } catch (Exception e) {
      log.error("Error al editar hecho {}", hechoDTO.getTitulo(), e);
      model.addAttribute("titulo", "Editar hecho");
      return "hechos/editar";
    }
  }

  @GetMapping("/{id}/solicitar-eliminacion")
  public String mostrarFormularioSolicitud(
      @PathVariable Long id,
      Model model
  ) {
    try {
      HechoDTO hechoDTO = hechosService.getHechoById(id).get();

      model.addAttribute("hecho", hechoDTO);
      model.addAttribute("titulo", "Solicitar eliminación");

      return "hechos/solicitar-eliminacion";
    } catch (NotFoundException e) {
      return "redirect:/404";
    }
  }

//  @PostMapping("/{id}/enviar-solicitud")
//  public String crearSolicitudEliminacion(@PathVariable Long id,
//                                          @ModelAttribute("solicitud") SolicitudEliminacionDTO solicitudEliminacionDTO,
//                                          BindingResult bindingResult,
//                                          Model model,
//                                          RedirectAttributes redirectAttributes
//  ) {
//    try {
//      SolicitudEliminacionDTO solicitudCreada = solicitudesService.crearSolicitud(solicitudEliminacionDTO);
//      return "redirect:/colecciones/colecciones";
//    } catch (Exception e) {
//      log.error("Error al crear solicitud de eliminación", e);
//      model.addAttribute("titulo", "Solicitar eliminación");
//      return "hechos/solicitar-eliminacion";
//    }
//    }
}
