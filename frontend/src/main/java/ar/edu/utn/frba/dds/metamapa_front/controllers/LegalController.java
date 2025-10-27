package ar.edu.utn.frba.dds.metamapa_front.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LegalController {


    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("titulo", "Política de Privacidad");
        return "legal/privacy";
    }

    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("titulo", "Términos y Condiciones");
        return "legal/terms";
    }

}
