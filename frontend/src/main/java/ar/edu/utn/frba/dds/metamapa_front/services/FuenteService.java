package ar.edu.utn.frba.dds.metamapa_front.services;

import ar.edu.utn.frba.dds.metamapa_front.dtos.FuenteDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.FuenteOutputDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class FuenteService {

  private static final Logger log = LoggerFactory.getLogger(FuenteService.class);
  private final WebClient webClient;
  private final String metamapaServiceUrl;

  @Autowired
  private MetamapaApiService metamapaApiService;

  @Autowired
  public FuenteService(@Value("${colecciones.service.url}") String metamapaServiceUrl) {
    this.webClient = WebClient.builder().build();
    this.metamapaServiceUrl = metamapaServiceUrl;
  }

  public List<FuenteOutputDTO> listarFuentes() {
    return metamapaApiService.getTodasLasFuentes();
  }

  public void crearFuente(FuenteDTO fuenteDTO) {
    try {
      Map<String, String> body = new java.util.HashMap<>();
      body.put("tipo", fuenteDTO.getTipo());
      body.put("ruta", fuenteDTO.getRuta());
      if (fuenteDTO.getTitulo() != null && !fuenteDTO.getTitulo().isEmpty()) {
        body.put("titulo", fuenteDTO.getTitulo());
      }

      webClient
          .post()
          .uri(metamapaServiceUrl + "/fuentes")
          .bodyValue(body)
          .retrieve()
          .bodyToMono(Void.class)
          .block();

      log.info("Fuente {} creada correctamente", fuenteDTO.getTipo());
    } catch (WebClientResponseException e) {
      log.error("Error al crear fuente: {}", e.getMessage());
      if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
        throw new IllegalArgumentException("Datos de fuente inválidos");
      }
      throw new RuntimeException("Error al crear fuente");
    }
  }

  public void crearFuenteEstatica(MultipartFile archivo, String titulo) {
    try {
      if (archivo.isEmpty()) {
        throw new IllegalArgumentException("El archivo está vacío");
      }

      if (!archivo.getOriginalFilename().endsWith(".csv")) {
        throw new IllegalArgumentException("El archivo debe ser un CSV");
      }

      MultipartBodyBuilder builder = new MultipartBodyBuilder();
      builder.part("archivo", new ByteArrayResource(archivo.getBytes()) {
        @Override
        public String getFilename() {
          return archivo.getOriginalFilename();
        }
      });

      if (titulo != null && !titulo.isEmpty()) {
        builder.part("titulo", titulo);
      }

      webClient
          .post()
          .uri(metamapaServiceUrl + "/fuentes/upload-csv")
          .contentType(MediaType.MULTIPART_FORM_DATA)
          .body(BodyInserters.fromMultipartData(builder.build()))
          .retrieve()
          .bodyToMono(Void.class)
          .block();

      log.info("Fuente estática creada correctamente desde archivo: {}", archivo.getOriginalFilename());
    } catch (IOException e) {
      log.error("Error al leer el archivo: {}", e.getMessage());
      throw new RuntimeException("Error al procesar el archivo");
    } catch (WebClientResponseException e) {
      log.error("Error al crear fuente estática: {}", e.getMessage());
      if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
        throw new IllegalArgumentException("Archivo CSV inválido");
      }
      throw new RuntimeException("Error al crear fuente estática");
    }
  }

  public List<FuenteOutputDTO> obtenerTodasLasFuentes()  {
    return metamapaApiService.getTodasLasFuentes();
  }

  public List<HechoDTO> getMisHechos() {
    return metamapaApiService.getMisHechos();
  }
}
