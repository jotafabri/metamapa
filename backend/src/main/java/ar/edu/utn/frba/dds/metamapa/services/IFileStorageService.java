package ar.edu.utn.frba.dds.metamapa.services;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {

  String guardarArchivo(MultipartFile archivo) throws IOException;

  List<String> guardarMultiples(List<MultipartFile> archivos) throws IOException;

  Resource cargarArchivo(String nombre) throws IOException;

  void eliminarArchivo(String nombre) throws IOException;
}
