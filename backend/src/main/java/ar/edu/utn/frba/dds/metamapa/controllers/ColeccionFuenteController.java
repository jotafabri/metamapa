package ar.edu.utn.frba.dds.metamapa.controllers;

import ar.edu.utn.frba.dds.metamapa.services.IColeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/colecciones")
@RequiredArgsConstructor
public class ColeccionFuenteController {
    private final IColeccionService coleccionService;

    @PostMapping("/{idColeccion}/fuentes/{idFuente}")
    @PreAuthorize("hasRole('ADMIN')")
    public void agregarFuente(@PathVariable Long idColeccion, @PathVariable Long idFuente) {
        coleccionService.agregarFuente(idColeccion, idFuente);
    }

    @DeleteMapping("/{idColeccion}/fuentes/{idFuente}")
    @PreAuthorize("hasRole('ADMIN')")
    public void quitarFuente(@PathVariable Long idColeccion, @PathVariable Long idFuente) {
        coleccionService.quitarFuente(idColeccion, idFuente);
    }
}