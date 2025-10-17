package ar.edu.utn.frba.dds.metamapa.controllers;

import ar.edu.utn.frba.dds.metamapa.models.entities.Usuario;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Rol;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IUsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/setup")
public class SetupController {

    private final IUsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public SetupController(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/crear-admin")
    public String crearAdmin() {
        String email = "admin@metamapa.com";
        if (usuarioRepository.existsByEmail(email)) {
            return "⚠️ El administrador ya existe: " + email;
        }

        Usuario admin = new Usuario(email, encoder.encode("admin123"), Rol.ADMIN);
        usuarioRepository.save(admin);
        return "✅ Administrador creado correctamente.\nEmail: " + email + "\nContraseña: admin123";
    }
}
