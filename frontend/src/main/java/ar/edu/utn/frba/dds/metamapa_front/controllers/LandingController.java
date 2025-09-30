package ar.edu.utn.frba.dds.metamapa_front.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {
  @GetMapping("/")
  public String landing() {
    return "landing/landing";
  }

  @GetMapping("/404")
  public String notFound(Model model) {
    model.addAttribute("titulo", "No encontrado");
    return "404";
  }

  @GetMapping("/403")
  public String forbidden(Model model) {
    model.addAttribute("titulo", "Acceso denegado");
    return "403";
  }
}
