package ar.edu.utn.frba.dds.metamapa_front.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa_front.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa_front.services.ColeccionService;
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
@RequestMapping("/colecciones")
@RequiredArgsConstructor
public class ColeccionController {
  private static final Logger log = LoggerFactory.getLogger(ColeccionController.class);
  private final ColeccionService coleccionService;

  @GetMapping
//  @PreAuthorize("hasAnyRole('VISUALIZADOR', 'CONTRIBUYENTE', 'ADMIN')")
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
      RedirectAttributes redirectAttributes) {
    try {
      // Obtener hechos paginados
      List<HechoDTO> hechosPaginados = coleccionService.getHechosByHandle(handle, filtros);

      Integer page = filtros.getPage();
      Integer size = filtros.getSize();

      model.addAttribute("titulo", "Colección");
      model.addAttribute("hechosPaginados", hechosPaginados);
      model.addAttribute("currentPage", page);
      model.addAttribute("pageSize", size);
      model.addAttribute("hasMore", hechosPaginados.size() == size);

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
      RedirectAttributes redirectAttributes
  ) {
    return mostrarHechos(
        handle,
        filtros,
        model,
        redirectAttributes);
  }

  @GetMapping("/nueva")
  @PreAuthorize("hasRole('ADMIN') and hasAnyAuthority('ADMINISTRAR_COLECCIONES')")
  public String mostrarFormularioCrear(Model model) {
    model.addAttribute("coleccion", new ColeccionDTO());
    model.addAttribute("titulo", "Crear colección");
    return "colecciones/crear";
  }

  @PostMapping("/crear")
  @PreAuthorize("hasRole('ADMIN') and hasAnyAuthority('ADMINISTRAR_COLECCIONES')")
  public String crearColeccion(@ModelAttribute("coleccion") ColeccionDTO coleccionDTO,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
    try {
      ColeccionDTO coleccionCreada = coleccionService.crearColeccion(coleccionDTO);
      return "redirect:/admin/colecciones";
    } catch (Exception e) {
      log.error("Error al crear nueva colección", e);
      model.addAttribute("titulo", "Crear colección");
      return "colecciones/crear";
    }
  }

  @GetMapping("/{handle}/editar")
  @PreAuthorize("hasRole('ADMIN') and hasAnyAuthority('ADMINISTRAR_COLECCIONES')")
  public String mostrarFormularioEditar(
      @PathVariable String handle,
      Model model) {
    try {
      ColeccionDTO coleccionDTO = coleccionService.getColeccionByHandle(handle).get();

      model.addAttribute("coleccion", coleccionDTO);
      model.addAttribute("titulo", "Editar colección");
      return "colecciones/editar";
    } catch (NotFoundException e) {
      return "redirect:/404";
    }
  }

  @PostMapping("/{handle}/actualizar")
  @PreAuthorize("hasRole('ADMIN') and hasAnyAuthority('ADMINISTRAR_COLECCIONES')")
  public String actualizarColeccion(@PathVariable String handle,
                                    @ModelAttribute("coleccion") ColeccionDTO coleccionDTO,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
    try {
      ColeccionDTO coleccionActualizada = coleccionService.actualizarColeccion(handle, coleccionDTO);

      return "redirect:/admin/colecciones";
    } catch (NotFoundException e) {
      return "redirect:/404";
    } catch (Exception e) {
      log.error("Error al editar colección {}", handle, e);
      model.addAttribute("titulo", "Editar colección");
      return "colecciones/editar";
    }
  }

  @PostMapping("/{handle}/eliminar")
  @PreAuthorize("hasRole('ADMIN') and hasAnyAuthority('ADMINISTRAR_COLECCIONES')")
  public String eliminarColeccion(@PathVariable String handle,
                                  @ModelAttribute("coleccion") ColeccionDTO coleccionDTO,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
    try {
      coleccionService.eliminarColeccion(handle);
      return "redirect:/admin/colecciones";
    } catch (NotFoundException e) {
      return "redirect:/404";
    } catch (Exception e) {
      return "colecciones/crear";
    }
  }
}
