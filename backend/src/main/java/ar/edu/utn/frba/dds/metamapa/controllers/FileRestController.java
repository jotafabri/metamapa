package ar.edu.utn.frba.dds.metamapa.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ar.edu.utn.frba.dds.metamapa.services.impl.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class FileRestController {

  @Autowired
  private FileStorageService fileStorageService;

  @GetMapping("/{filename:.+}")
  public ResponseEntity<Resource> descargarArchivo(@PathVariable String filename) {
    try {
      Resource resource = fileStorageService.cargarArchivo(filename);

      String contentType = "application/octet-stream";
      try {
        Path path = Paths.get(resource.getURI());
        contentType = Files.probeContentType(path);
      } catch (IOException ignored) {}

      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
          .body(resource);

    } catch (IOException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
