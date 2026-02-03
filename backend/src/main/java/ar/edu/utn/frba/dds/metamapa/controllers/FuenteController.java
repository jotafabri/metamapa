package ar.edu.utn.frba.dds.metamapa.controllers;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.metamapa.services.IFuenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/fuentes")
@RequiredArgsConstructor
public class FuenteController {

    private final IFuenteService fuenteService;
    private static final String UPLOAD_DIR = "uploads/csv/";

    @GetMapping
    public List<FuenteOutputDTO> listar() {
        return fuenteService.listarFuentes().stream()
                .map(FuenteOutputDTO::fromFuente)
                .toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public FuenteOutputDTO crear(@RequestBody FuenteInputDTO dto) {
        return FuenteOutputDTO.fromFuente(fuenteService.crearFuente(dto));
    }

    @PostMapping("/upload-csv")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuenteOutputDTO> uploadCSV(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(value = "titulo", required = false) String titulo) {
        try {
            if (archivo.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Crear directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Guardar archivo
            String filename = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(archivo.getInputStream(), filePath);

            // Crear fuente estática con la ruta del archivo
            FuenteInputDTO dto = new FuenteInputDTO();
            dto.setTipo("estatica");
            dto.setRuta(filePath.toString());
            if (titulo != null && !titulo.isEmpty()) {
                dto.setTitulo(titulo);
            }

            FuenteOutputDTO fuenteCreada = FuenteOutputDTO.fromFuente(fuenteService.crearFuente(dto));
            return ResponseEntity.status(HttpStatus.CREATED).body(fuenteCreada);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        fuenteService.eliminarFuente(id);
    }
}
