package ar.edu.utn.frba.dds.metamapa_front.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {
  @GetMapping("/")
  public String landing() {
    return "redirect:/landing";
  }

  public String notFound(Model model) {
    model.addAttribute("titulo", "No encontrado");
    return "404";
  }
}
