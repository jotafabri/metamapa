package ar.edu.utn.frba.dds.metamapa_front.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa_front.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.DatosGeograficosDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa_front.services.ColeccionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/colecciones")
@RequiredArgsConstructor
public class ColeccionController {
  private static final Logger log = LoggerFactory.getLogger(ColeccionController.class);
  private final ColeccionService coleccionService;

  @Value("${colecciones.service.url}")
  private String backendUrl;

  @GetMapping
  public String listarColecciones(Model model) {
    List<ColeccionDTO> colecciones;
    try {
      colecciones = coleccionService.getAllColecciones();
    } catch (Exception e) {
      // ignorado
      colecciones = List.of();
    }
    model.addAttribute("colecciones", colecciones);
    model.addAttribute("titulo", "Colecciones");

    return "colecciones/colecciones";
  }

  @GetMapping("/{handle}")
  public String mostrarHechos(
      @PathVariable String handle,
      @ModelAttribute("filtros") HechoFiltroDTO filtros,
      Model model,
      RedirectAttributes redirectAttributes,
      Authentication auth
  ) {
    try {
      // Obtener hechos paginados
      List<HechoDTO> hechosPaginados = coleccionService.getHechosByHandle(handle, filtros);
      DatosGeograficosDTO datosGeograficos = coleccionService.getCategoriasByHandle(handle);

      Integer page = filtros.getPage();
      Integer size = filtros.getSize();

      Long currentUserId = null;
      boolean isAdmin = false;

      model.addAttribute("titulo", "Colección");
      model.addAttribute("handle", handle);
      model.addAttribute("hechosPaginados", hechosPaginados);
      model.addAttribute("listaCategorias", datosGeograficos.getCategorias());
      model.addAttribute("listaPaises", datosGeograficos.getPaises());
      model.addAttribute("listaProvincias", datosGeograficos.getProvincias());
      model.addAttribute("listaLocalidades", datosGeograficos.getLocalidades());
      model.addAttribute("currentPage", page);
      model.addAttribute("pageSize", size);
      model.addAttribute("hasMore", hechosPaginados.size() == size);
      model.addAttribute("currentUserId", currentUserId);
      model.addAttribute("isAdmin", isAdmin);
      model.addAttribute("solicitudEliminacion", new SolicitudEliminacionDTO());
      model.addAttribute("backendUrl", backendUrl);

      return "colecciones/viewer";
    } catch (NotFoundException ex) {
      redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
      return "redirect:/404";
    }
  }

  @PostMapping("/{handle}")
  public String listarHechos(
      @PathVariable String handle,
      @ModelAttribute("filtros") HechoFiltroDTO filtros,
      Model model,
      RedirectAttributes redirectAttributes,
      Authentication auth
  ) {
    return mostrarHechos(
        handle,
        filtros,
        model,
        redirectAttributes,
        auth
    );
  }

}
