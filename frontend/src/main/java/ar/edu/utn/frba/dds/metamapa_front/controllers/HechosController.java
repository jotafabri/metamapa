package ar.edu.utn.frba.dds.metamapa_front.controllers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa_front.services.HechosService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hechos")
@RequiredArgsConstructor
public class HechosController {
  private static final Logger log = LoggerFactory.getLogger(HechosController.class);
  private final HechosService hechosService;

  @Value("${colecciones.service.url}")
  private String backendUrl;

  @GetMapping("/me")
  public String verHechosDeUsuario(Model model) {
    try {
      List<HechoDTO> hecho = hechosService.getMisHechos();

      Long currentUserId = null;
      Boolean isAdmin = false;

      model.addAttribute("hechos", hecho);
      model.addAttribute("titulo", "Mis hechos");
      model.addAttribute("backendUrl", backendUrl);
      model.addAttribute("currentUserId", currentUserId);
      model.addAttribute("isAdmin", isAdmin);
      model.addAttribute("solicitudEliminacion", new SolicitudEliminacionDTO());

      return "hechos/hechos";
    } catch (NotFoundException e) {
      return "redirect:/404";
    }
  }

  @GetMapping("/{id}")
  public String verDetalleHecho(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
    try {
      HechoDTO hecho = hechosService.getHechoById(id).get();

      Long currentUserId = null;
      boolean isAdmin = false;
      boolean puedeEditar = hecho.getFechaCarga() != null &&
              ChronoUnit.DAYS.between(hecho.getFechaCarga(), LocalDateTime.now()) <= 7;

      model.addAttribute("hecho", hecho);
      model.addAttribute("titulo", "Detalle del hecho");
      model.addAttribute("backendUrl", backendUrl);
      model.addAttribute("currentUserId", currentUserId);
      model.addAttribute("isAdmin", isAdmin);
      model.addAttribute("puedeEditar", puedeEditar);
      model.addAttribute("solicitudEliminacion", new SolicitudEliminacionDTO());

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
  public String crearHecho(
      @ModelAttribute("hecho") HechoDTO hechoDTO,
      @RequestParam(required = false) List<MultipartFile> archivos,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    try {
      hechosService.crearHecho(hechoDTO, archivos);

      redirectAttributes.addFlashAttribute("toastMessage", "Hecho creado con éxito ✅");
      redirectAttributes.addFlashAttribute("toastType", "success");
      return "redirect:/colecciones";
    } catch (IllegalArgumentException ex) {
      if ("FECHA_FUTURA".equals(ex.getMessage())) {
        redirectAttributes.addFlashAttribute("toastMessage",
                "No se pudo crear el hecho: la fecha es inválida (futura) ❌");
        redirectAttributes.addFlashAttribute("toastType", "error");
        return "redirect:/hechos/nuevo";
      }

      redirectAttributes.addFlashAttribute("toastMessage",
              "No se pudo crear el hecho ❌");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/hechos/nuevo";
    }


  }
/*
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
*/

  @GetMapping("/{id}/editar")
  @PreAuthorize("hasRole('USER')")
  public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
    try {
      log.info("Intentando editar hecho con id: {}", id);
      HechoDTO hechoDTO = hechosService.getHechoById(id)
              .orElseThrow(() -> new NotFoundException("Hecho no encontrado"));

      model.addAttribute("hecho", hechoDTO);
      model.addAttribute("titulo", "Editar hecho");
      model.addAttribute("modoEdicion", true); // 🔹 indicador para el Thymeleaf
      return "hechos/contribuir";
    } catch (NotFoundException e) {
      return "redirect:/404";
    }
  }

  /*
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

   */
  @PostMapping("/{id}/actualizar")
  //@PreAuthorize("hasAuthority('EDITAR_HECHOS')")
  public String actualizarHecho(
          @PathVariable Long id,
          @ModelAttribute("hecho") HechoDTO hechoDTO,
          @RequestParam(required = false) List<MultipartFile> archivos,
          BindingResult bindingResult,
          RedirectAttributes redirectAttributes,
          Model model) {

    try {

      HechoDTO hechoOriginal = hechosService.getHechoById(id)
              .orElseThrow(() -> new NotFoundException("Hecho no encontrado"));

      // Verificación del plazo de 7 días
      if (hechoOriginal.getFechaCarga() != null &&
              ChronoUnit.DAYS.between(hechoOriginal.getFechaCarga(), LocalDateTime.now()) > 7) {
        redirectAttributes.addFlashAttribute("toastMessage", "Este hecho ya no se puede editar ❌");
        redirectAttributes.addFlashAttribute("toastType", "error");
        return "redirect:/hechos/" + id;
      }

      hechosService.actualizarHecho(id, hechoDTO);

      redirectAttributes.addFlashAttribute("toastMessage", "Hecho actualizado correctamente ✅");
      redirectAttributes.addFlashAttribute("toastType", "success");
      return "redirect:/hechos/me";
    } catch (NotFoundException e) {
      return "redirect:/404";
    } catch (Exception e) {
      log.error("Error al actualizar el hecho {}", hechoDTO.getTitulo(), e);
      redirectAttributes.addFlashAttribute("toastMessage", "No se pudo actualizar el hecho ❌");
      redirectAttributes.addFlashAttribute("toastType", "error");
      return "redirect:/hechos/" + id + "/editar";
    }
  }



}
