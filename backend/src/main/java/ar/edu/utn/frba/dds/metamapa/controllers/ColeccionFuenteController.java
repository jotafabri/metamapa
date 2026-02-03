package ar.edu.utn.frba.dds.metamapa.controllers;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import ar.edu.utn.frba.dds.metamapa.services.IFuenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colecciones")
@RequiredArgsConstructor
public class ColeccionFuenteController {
    private final IAgregacionService agregacionService;
    private final IFuenteService fuenteService;
    private final IColeccionesRepository coleccionesRepository;

    @GetMapping("/{handleColeccion}/fuentes")
    public ResponseEntity<List<FuenteOutputDTO>> listarFuentes(@PathVariable String handleColeccion) {
        var coleccion = coleccionesRepository.findColeccionByHandle(handleColeccion);
        if (coleccion == null)
            return ResponseEntity.notFound().build();

        List<FuenteOutputDTO> fuentes = coleccion.getFuentes().stream()
                .map(FuenteOutputDTO::fromFuente)
                .toList();
        return ResponseEntity.ok(fuentes);
    }

    @PostMapping("/{handleColeccion}/fuentes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuenteOutputDTO> crearFuenteEnColeccion(@PathVariable String handleColeccion,
            @RequestBody FuenteInputDTO fuenteDTO) {
        var fuente = fuenteService.crearFuente(fuenteDTO);
        agregacionService.agregarFuenteAColeccion(handleColeccion, fuente.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(FuenteOutputDTO.fromFuente(fuente));
    }

    @PostMapping("/{handleColeccion}/fuentes/{idFuente}")
    @PreAuthorize("hasRole('ADMIN')")
    public void agregarFuente(@PathVariable String handleColeccion, @PathVariable Long idFuente) {
        agregacionService.agregarFuenteAColeccion(handleColeccion, idFuente);
    }

    @PatchMapping("/{handleColeccion}/fuentes/{idFuente}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuenteOutputDTO> editarFuente(@PathVariable String handleColeccion,
            @PathVariable Long idFuente, @RequestBody FuenteInputDTO fuenteDTO) {
        var fuente = fuenteService.actualizarFuente(idFuente, fuenteDTO);
        var coleccion = coleccionesRepository.findColeccionByHandle(handleColeccion);
        if (coleccion != null) {
            coleccion.actualizarColeccion();
            coleccionesRepository.save(coleccion);
        }
        return ResponseEntity.ok(FuenteOutputDTO.fromFuente(fuente));
    }

    @DeleteMapping("/{handleColeccion}/fuentes/{idFuente}")
    @PreAuthorize("hasRole('ADMIN')")
    public void quitarFuente(@PathVariable String handleColeccion, @PathVariable Long idFuente) {
        agregacionService.eliminarFuenteDeColeccion(handleColeccion, idFuente);
    }
}