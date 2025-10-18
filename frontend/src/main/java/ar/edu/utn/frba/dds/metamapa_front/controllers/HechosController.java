package ar.edu.utn.frba.dds.metamapa_front.controllers;

import java.util.List;
import java.util.Map;

import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa_front.services.HechosService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
  @ResponseBody
  public ResponseEntity<Map<String, Object>> crearHecho(
          @ModelAttribute("hecho") HechoDTO hechoDTO,
          @RequestParam(required = false) List<MultipartFile> archivos) {
    try {
      hechosService.crearHecho(hechoDTO, archivos);
      Map<String, Object> resp = Map.of(
              "mensaje", "El hecho se envió correctamente",
              "redirect", "/colecciones"
      );
      return ResponseEntity.ok(resp);
    } catch (Exception e) {
      log.error("Error al crear nuevo hecho", e);
      Map<String, Object> resp = Map.of(
              "error", "Ocurrió un error al crear nuevo hecho"
      );
      return ResponseEntity.status(500).body(resp);
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

}
