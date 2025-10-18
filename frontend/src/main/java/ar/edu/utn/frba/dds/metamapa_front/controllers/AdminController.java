package ar.edu.utn.frba.dds.metamapa_front.controllers;

import ar.edu.utn.frba.dds.metamapa_front.dtos.UsuarioDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.UserDTO;
import ar.edu.utn.frba.dds.metamapa_front.services.UsuarioService;
import ar.edu.utn.frba.dds.metamapa_front.services.ColeccionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private final UsuarioService usuarioService;
    private final ColeccionService coleccionService;

    // --- LOGIN ADMIN ---

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("titulo", "Iniciar sesión como administrador");
        model.addAttribute("usuario", new UsuarioDTO());
        return "admin/login"; // Template: src/main/resources/templates/admin/login.html
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute("usuario") UsuarioDTO usuarioDTO, Model model) {
        try {
            var authResponse = usuarioService.autenticar(usuarioDTO);

            if (authResponse != null) {
                return "redirect:/admin/dashboard";
            } else {
                model.addAttribute("error", "Credenciales inválidas o sin permisos de administrador.");
                return "admin/login";
            }

        } catch (Exception e) {
            log.error("Error al iniciar sesión como admin", e);
            model.addAttribute("titulo", "Iniciar sesión como administrador");
            model.addAttribute("error", "Ocurrió un error al procesar el inicio de sesión.");
            return "admin/login";
        }
    }

    // --- DASHBOARD ---

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        model.addAttribute("titulo", "Panel de administración");
        return "admin/dashboard"; // Template: src/main/resources/templates/admin/dashboard.html
    }

    @GetMapping("/colecciones")
    public String mostrarColecciones(Model model) {
        model.addAttribute("colecciones", coleccionService.getAllColecciones());
        return "admin/colecciones";
    }

}
