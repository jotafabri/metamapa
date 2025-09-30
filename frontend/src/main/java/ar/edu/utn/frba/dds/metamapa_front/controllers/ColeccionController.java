package ar.edu.utn.frba.dds.metamapa_front.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa_front.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa_front.services.ColeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/colecciones")
@RequiredArgsConstructor
public class ColeccionController {

  private final ColeccionService coleccionService;

  @GetMapping
  public String listarColecciones(Model model) {
    List<ColeccionDTO> colecciones = coleccionService.getAllColecciones();
    model.addAttribute("colecciones", colecciones);
    model.addAttribute("titulo", "Colecciones");

    return "colecciones";
  }

  @GetMapping("/{handle}")
  public String listarHechos(
      @PathVariable String handle,
      @ModelAttribute HechoFiltroDTO filtros,
      @RequestParam(required = false, defaultValue = "false") Boolean curado,
      Model model,
      RedirectAttributes redirectAttributes) {
    try{
      List<HechoDTO> hechos = coleccionService.getHechosByHandle(handle, filtros, curado);

      model.addAttribute("hechos", hechos);
      model.addAttribute("titulo", "Colección");
      model.addAttribute("totalHechos", hechos.size());

      return "colecciones/viewer";
    } catch (NotFoundException ex) {
      redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
      return "redirect:/404";
    }
  }

}
