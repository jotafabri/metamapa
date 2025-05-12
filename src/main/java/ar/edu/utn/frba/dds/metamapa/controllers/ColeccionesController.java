package ar.edu.utn.frba.dds.metamapa.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ar.edu.utn.frba.dds.metamapa.services.impl.AgregacionService;
import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ColeccionesController {
    private final AgregacionService servicio;
    private final List<Coleccion> colecciones= new ArrayList<>();

    public ColeccionesController(AgregacionService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Coleccion> colecciones = servicio.obtenerColecciones();
        model.addAttribute("colecciones", colecciones);
        return "index"; // plantilla Thymeleaf en /templates/index.html
    }
}

