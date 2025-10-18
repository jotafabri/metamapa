package ar.edu.utn.frba.dds.metamapa.controllers;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.metamapa.services.IFuenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuentes")
@RequiredArgsConstructor
public class FuenteController {

    private final IFuenteService fuenteService;

    @GetMapping
    public List<FuenteOutputDTO> listar() {
        return fuenteService.listarFuentes().stream()
                .map(FuenteOutputDTO::fromFuente)
                .toList();
    }

    @PostMapping
    public FuenteOutputDTO crear(@RequestBody FuenteInputDTO dto) {
        return FuenteOutputDTO.fromFuente(fuenteService.crearFuente(dto));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        fuenteService.eliminarFuente(id);
    }
}
