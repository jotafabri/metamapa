package ar.edu.utn.frba.dds.metamapa_front.controllers;

import ar.edu.utn.frba.dds.metamapa_front.dtos.LoginRequest;
import ar.edu.utn.frba.dds.metamapa_front.dtos.RegistroRequest;
import ar.edu.utn.frba.dds.metamapa_front.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("titulo", "Iniciar sesión");
        model.addAttribute("usuario", new LoginRequest());
        return "accounts/login";
    }

    @PostMapping("/register")
    public String crearUsuario(Model model, @ModelAttribute("usuario") RegistroRequest registroRequest) {
        try {
            usuarioService.crearUsuario(registroRequest);
            return "redirect:/colecciones";
        } catch (Exception e) {
            log.error("Error al crear nuevo usuario", e);
            model.addAttribute("titulo", "Registrarse");
            model.addAttribute("usuario", new RegistroRequest());
            return "accounts/register";
        }
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("titulo", "Registrarse");
        model.addAttribute("usuario", new RegistroRequest());
        return "accounts/register";
    }
}
