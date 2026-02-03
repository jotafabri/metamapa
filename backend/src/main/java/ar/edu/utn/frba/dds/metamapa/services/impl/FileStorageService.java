package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.metamapa.services.IFileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService implements IFileStorageService {

  private final Path uploadPath;
  private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

  public FileStorageService(@Value("${upload.path}") String uploadDir) {
    this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
    log.info("Upload path configurado en: {}", this.uploadPath);
    try {
      Files.createDirectories(this.uploadPath);
    } catch (IOException e) {
      throw new RuntimeException("No se pudo crear directorio de uploads", e);
    }
  }

  public String guardarArchivo(MultipartFile archivo) throws IOException {
    String nombreOriginal = archivo.getOriginalFilename();
    String nombreUnico = UUID.randomUUID().toString() + "_" + nombreOriginal;
    Path destino = this.uploadPath.resolve(nombreUnico);

    log.info("   Guardando archivo:");
    log.info("   Nombre original: {}", nombreOriginal);
    log.info("   Nombre final: {}", nombreUnico);
    log.info("   Path destino: {}", destino);


    Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
    return nombreUnico;
  }

  public List<String> guardarMultiples(List<MultipartFile> archivos) throws IOException {
    return archivos.stream()
        .filter(a -> !a.isEmpty())
        .map(a -> {
          try { return guardarArchivo(a); }
          catch (IOException e) { throw new UncheckedIOException(e); }
        })
        .collect(Collectors.toList());
  }

  public Resource cargarArchivo(String nombre) throws IOException {
    Path archivo = uploadPath.resolve(nombre).normalize();
    Resource resource = new UrlResource(archivo.toUri());
    if (resource.exists() && resource.isReadable()) {
      return resource;
    }
    throw new FileNotFoundException("Archivo no encontrado: " + nombre);
  }

  public void eliminarArchivo(String nombre) throws IOException {
    Path archivo = uploadPath.resolve(nombre).normalize();
    Files.deleteIfExists(archivo);
  }
}
