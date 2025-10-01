package ar.edu.utn.frba.dds.metamapa_front.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contribuir")
@RequiredArgsConstructor
public class ContribuirController {
  private static final Logger log = LoggerFactory.getLogger(ContribuirController.class);

  @GetMapping
  public String contribuir(Model model) {
    model.addAttribute("titulo", "Contribuir");
    return "contribuir/contribuir";
  }
}
