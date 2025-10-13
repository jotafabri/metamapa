package ar.edu.utn.frba.dds.metamapa_front.controllers;

import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.UsuarioDTO;
import ar.edu.utn.frba.dds.metamapa_front.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("titulo", "Iniciar sesión");
        model.addAttribute("usuario", new UsuarioDTO());
        return "accounts/login";
    }

    @PostMapping("/register")
    public String crearUsuario(Model model, @ModelAttribute("usuario") UsuarioDTO usuarioDTO) {
        try {
            usuarioService.crearUsuario(usuarioDTO);
            return "redirect:/colecciones";
        } catch (Exception e) {
            log.error("Error al crear nuevo usuario", e);
            model.addAttribute("titulo", "Registro");
            model.addAttribute("usuario", new UsuarioDTO());
            return "accounts/register";
        }
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("titulo", "Registro");
        model.addAttribute("usuario", new UsuarioDTO());
        return "accounts/register";
    }
}
