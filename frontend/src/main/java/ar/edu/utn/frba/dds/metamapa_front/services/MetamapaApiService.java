package ar.edu.utn.frba.dds.metamapa_front.services;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.metamapa_front.dtos.*;
import ar.edu.utn.frba.dds.metamapa_front.dtos.input.UserInputDTO;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa_front.services.internal.GraphQlCallerService;
import ar.edu.utn.frba.dds.metamapa_front.services.internal.WebApiCallerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class MetamapaApiService {

  private static final Logger log = LoggerFactory.getLogger(MetamapaApiService.class);
  private final WebClient webClient;
  private final WebApiCallerService webApiCallerService;
  private final GraphQlCallerService graphQlCallerService;
  private final String metamapaServiceUrl;

  @Autowired
  public MetamapaApiService(WebApiCallerService webApiCallerService, GraphQlCallerService graphQlCallerService, @Value("${colecciones.service.url}") String metamapaServiceUrl) {
    this.webClient = WebClient.builder().build();
    this.webApiCallerService = webApiCallerService;
    this.graphQlCallerService = graphQlCallerService;
    this.metamapaServiceUrl = metamapaServiceUrl;
  }

  public AuthResponseDTO login(String username, String password) {
    try {
      // Llamar al endpoint /api/auth/login que devuelve los JWT reales
      AuthResponseDTO authResponse = webClient
          .post()
          .uri(metamapaServiceUrl + "/auth/login")
          .bodyValue(Map.of(
              "email", username,
              "password", password
          ))
          .retrieve()
          .bodyToMono(AuthResponseDTO.class)  // Deserializar directamente a AuthResponseDTO
          .block();

      if (authResponse == null) {
        return null;
      }

      return authResponse;

    } catch (WebClientResponseException e) {
      log.error(e.getMessage());
      if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.NOT_FOUND) {
        return null;
      }
      if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
        throw new RuntimeException("RATE_LIMIT_EXCEEDED");
      }
      throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
    }
  }
/*
  public RolesPermisosDTO getRolesPermisos(String email) {
    try {
      // Llamar al nuevo endpoint /api/auth/user con el email
      Map<String, Object> response = webClient
              .post()
              .uri(metamapaServiceUrl + "/auth/user")
              .bodyValue(Map.of("email", email)).retrieve().bodyToMono(Map.class)
              .block();

      if (response == null) {
        throw new RuntimeException("Usuario no encontrado");
      }

      RolesPermisosDTO rolesPermisos = new RolesPermisosDTO();

      rolesPermisos.setEmail((String) response.get("email"));
      String rolStr = (String) response.get("rol");
      rolesPermisos.setRol(Rol.valueOf(rolStr));
      rolesPermisos.setPermisos(List.of());

      return rolesPermisos;

    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException("Error al obtener roles y permisos: " + e.getMessage(), e);
    }
  }
  */

  public RolesDTO getRoles(String accessToken) {
    try {
      UserInputDTO user = webClient
              .get()
              .uri(metamapaServiceUrl + "/auth/user")
              .header("Authorization", "Bearer " + accessToken)
              .retrieve()
              .bodyToMono(UserInputDTO.class)
              .block();

      if (user == null) {
        throw new RuntimeException("Usuario no encontrado");
      }

      return RolesDTO.builder()
              .email(user.getEmail())
              .rol(user.getRol())
              .permisos(List.of())
              .build();

    } catch (Exception e) {
      throw new RuntimeException("Error al obtener roles y permisos", e);
    }
  }




  public List<ColeccionDTO> getAllColecciones() {
    return retrieveColecciones("/colecciones");
  }

  public List<ColeccionDTO> getAllColecciones(Integer limit) {
    return retrieveColecciones("/colecciones?limit=" + limit);
  }

  private List<ColeccionDTO> retrieveColecciones(String endpoint) {
    List<ColeccionDTO> response = webApiCallerService.getListPublic(metamapaServiceUrl + endpoint, ColeccionDTO.class);
    return response != null ? response : List.of();
  }

  public List<HechoDTO> getHechosByHandle(String handle, HechoFiltroDTO filtros) {
    List<HechoDTO> response = webApiCallerService.getListPublic(this.generarUrl(handle, filtros), HechoDTO.class);
    return response != null ? response : List.of();
  }

  public DatosGeograficosDTO getDatosUbicacionByHandle(String handle) {
    DatosGeograficosDTO response = webApiCallerService.getPublic(metamapaServiceUrl + "/colecciones/" + handle + "/ubicaciones", DatosGeograficosDTO.class);
    if (response == null) {
      throw new NotFoundException("DatosGeograficos", handle);
    }
    return response;
  }

  public ColeccionDTO getColeccionByHandle(String handle) {
    ColeccionDTO response = webApiCallerService.getPublic(metamapaServiceUrl + "/colecciones/" + handle, ColeccionDTO.class);
    if (response == null) {
      throw new NotFoundException("Coleccion", handle);
    }
    return response;
  }

  public ColeccionDTO crearColeccion(ColeccionDTO coleccionDTO) {
    ColeccionDTO response = webApiCallerService.post(metamapaServiceUrl + "/colecciones", coleccionDTO, ColeccionDTO.class);
    if (response == null) {
      throw new RuntimeException("Error al crear coleccion en el servicio externo");
    }

    if (coleccionDTO.getFuentesIds() != null && !coleccionDTO.getFuentesIds().isEmpty()) {
      reemplazarFuentesColeccion(response.getHandle(), coleccionDTO.getFuentesIds());
    }

    return response;
  }

  public ColeccionDTO actualizarColeccion(String handle, ColeccionDTO coleccionDTO) {
    ColeccionDTO response = webApiCallerService.patch(
            metamapaServiceUrl + "/colecciones/" + handle,
            coleccionDTO,
            ColeccionDTO.class
    );

    if (response == null) {
      throw new RuntimeException("Error al actualizar coleccion en el servicio externo");
    }


    return response;
  }


  public void eliminarColeccion(String handle) {
    webApiCallerService.delete(metamapaServiceUrl + "/colecciones/" + handle);
  }

  public HechoDTO getHechoById(Long id) {

    String url = metamapaServiceUrl.endsWith("/")
        ? metamapaServiceUrl + "hechos/" + id
        : metamapaServiceUrl + "/hechos/" + id;

    log.info("Llamando al backend: {}", url);

    try {
      HechoDTO response = webApiCallerService.getPublic(url, HechoDTO.class);
      if (response == null) {
        throw new NotFoundException("Hecho", id.toString());
      }
      return response;

    } catch (WebClientResponseException e) {
      log.error("Error HTTP {} al llamar a {}: {}", e.getStatusCode(), url, e.getResponseBodyAsString());
      throw e;
    }
  }


  public HechoDTO crearHecho(HechoDTO hechoDTO, List<MultipartFile> archivos) {
    if (archivos == null || archivos.isEmpty()) {
      return crearHechoSinArchivos(hechoDTO);
    }
    return crearHechoConArchivos(hechoDTO, archivos);
  }

  private HechoDTO crearHechoSinArchivos(HechoDTO hechoDTO) {
    // Verificar si hay sesión activa (usuario logeado)
    HechoDTO response;
    try {
      // Intentar con autenticación (sesión activa)
      response = webApiCallerService.post(metamapaServiceUrl + "/hechos", hechoDTO, HechoDTO.class);
    } catch (RuntimeException e) {
      // Si falla porque no hay token, usar versión pública (anónimo)
      if (e.getMessage().contains("No hay token de acceso")) {
        response = webApiCallerService.postPublic(metamapaServiceUrl + "/hechos", hechoDTO, HechoDTO.class);
      } else {
        throw e;
      }
    }

    if (response == null) {
      throw new RuntimeException("Error al crear hecho en el servicio externo");
    }
    return response;
  }

  private HechoDTO crearHechoConArchivos(HechoDTO hechoDTO, List<MultipartFile> archivos) {
    MultiValueMap<String, HttpEntity<?>> body = new LinkedMultiValueMap<>();

    // Parte JSON - asegurarse que se serialice correctamente
    HttpHeaders jsonHeaders = new HttpHeaders();
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      String hechoJson = mapper.writeValueAsString(hechoDTO);
      body.add("hecho", new HttpEntity<>(hechoJson, jsonHeaders));
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error serializando HechoDTO", e);
    }

    // Archivos
    archivos.stream().filter(archivo -> !archivo.isEmpty()).forEach(archivo -> {
      try {
        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentDispositionFormData("archivos", archivo.getOriginalFilename());
        fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        body.add("archivos", new HttpEntity<>(archivo.getBytes(), fileHeaders));
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    });

    // Verificar si hay sesión activa (usuario logeado)
    HechoDTO response;
    try {
      // Intentar con autenticación (sesión activa)
      response = webApiCallerService.postMultipart(metamapaServiceUrl + "/hechos", body, HechoDTO.class);
    } catch (RuntimeException e) {
      // Si falla porque no hay token, usar versión pública (anónimo)
      if (e.getMessage().contains("No hay token de acceso")) {
        response = webApiCallerService.postPublicMultipart(metamapaServiceUrl + "/hechos", body, HechoDTO.class);
      } else {
        throw e;
      }
    }

    if (response == null) {
      throw new RuntimeException("Error al crear hecho en el servicio externo");
    }

    return response;
  }

  public HechoDTO actualizarHecho(Long id, HechoDTO hechoDTO) {


    log.info("AccessToken en sesión: {}", webApiCallerService.debugGetAccessToken());
    HechoDTO response = webApiCallerService.patch(metamapaServiceUrl + "/hechos/" + id.toString(), hechoDTO, HechoDTO.class);


    if (response == null) {
      throw new RuntimeException("Error al actualizar hecho en el servicio externo");
    }
    return response;
  }

  public HechoDTO aprobarHecho(Long id, HechoDTO hechoActualizado) {
    HechoDTO response = webApiCallerService.patch(metamapaServiceUrl + "/hechos/" + id.toString() + "/aprobar", hechoActualizado, HechoDTO.class);
    if (response == null) {
      throw new RuntimeException("Error al aprobar hecho en el servicio externo");
    }
    return response;
  }

  public HechoDTO rechazarHecho(Long id) {
    HechoDTO response = webApiCallerService.patch(metamapaServiceUrl + "/hechos/" + id.toString() + "/rechazar", new java.util.HashMap<>(), HechoDTO.class);
    if (response == null) {
      throw new RuntimeException("Error al rechazar hecho en el servicio externo");
    }
    return response;
  }


  public List<HechoDTO> getMisHechos() {
    List<HechoDTO> response = webApiCallerService.getList(metamapaServiceUrl + "/hechos/me", HechoDTO.class);
    return response != null ? response : List.of();
  }

  public List<HechoDTO> getAllHechos(Integer limit) {
    return webApiCallerService.getListPublic(metamapaServiceUrl + "/hechos?size=" + limit.toString(), HechoDTO.class);
  }


  public List<HechoDTO> obtenerHechosPendientes() {
    List<HechoDTO> response = webApiCallerService.getList(metamapaServiceUrl + "/hechos/pendientes", HechoDTO.class);
    return response != null ? response : List.of();
  }

  public List<SolicitudEliminacionDTO> obtenerSolicitudes() {
    String query = "{ getSolicitudes { id razon idHecho estado fecha } }";
    return graphQlCallerService.executeQueryForList(query, SolicitudEliminacionDTO.class);
  }


  public void crearSolicitudEliminacion(SolicitudEliminacionDTO solicitudDTO) {
    String razonEscapada = solicitudDTO.getRazon()
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n");

    String query = String.format(
        "mutation { crearSolicitud(solicitud: { idHecho: %s, razon: \"\"\"%s\"\"\" }) { id razon idHecho estado } }",
        solicitudDTO.getIdHecho().toString(),
        razonEscapada
    );

    graphQlCallerService.executePublicQuery(query, SolicitudEliminacionDTO.class);
  }

  public void aceptarSolicitudEliminacion(Long id) {
    String query = "mutation { aceptarSolicitud(solicitud: { id: \"" + id.toString() + "\" }) { id razon idHecho estado } }";
    graphQlCallerService.executeQuery(query, SolicitudEliminacionDTO.class);
  }


  public void rechazarSolicitudEliminacion(Long id) {
    String query = "mutation { rechazarSolicitud(solicitud: { id: \"" + id.toString() + "\" }) { id razon idHecho estado } }";
    graphQlCallerService.executeQuery(query, SolicitudEliminacionDTO.class);
  }

  public void reemplazarFuentesColeccion(String handleColeccion, List<Long> idsFuentesDeseadas) {
    String fuentesArray = idsFuentesDeseadas.stream()
        .map(id -> "{ id: " + id + " }")
        .collect(Collectors.joining(", "));
    String query = "mutation { reemplazarFuentesColeccion(coleccion: { handle: \"" + handleColeccion + "\" }, fuentes: [" + fuentesArray + "] ) { id fuentes { id } } }";
    graphQlCallerService.executeQuery(query, ColeccionDTO.class);
  }

  public void crearUsuario(RegistroRequest registroRequest) {
    webApiCallerService.postPublic(metamapaServiceUrl + "/auth/registro", registroRequest, RegistroRequest.class);
  }

  /*
  public AuthResponseDTO autenticar(LoginRequest loginRequest) {
    AuthResponseDTO response = webApiCallerService.postPublic(metamapaServiceUrl + "/auth/login", loginRequest, AuthResponseDTO.class);
    if (response == null) {
      throw new RuntimeException("Error al iniciar sesión en el servicio externo");
    }
    return response;
  }
*/

  public void actualizarEstadisticas() {
    webApiCallerService.get(metamapaServiceUrl + "/estadisticas/actualizar", null);
  }

  public String obtenerProvinciaConMasHechosEnColeccion(String coleccionHandle) {
    String response = webApiCallerService.get(metamapaServiceUrl + "/estadisticas/provincia-mas-hechos-coleccion?coleccionHandle=" + coleccionHandle, String.class);
    if (response == null) {
      throw new RuntimeException("Error al obtener estadistica en el servicio externo");
    }
    return response;
  }

  public String obtenerCategoriaConMasHechos() {
    String response = webApiCallerService.get(metamapaServiceUrl + "/estadisticas/categoria-mas-hechos", String.class);
    if (response == null) {
      throw new RuntimeException("Error al obtener estadistica en el servicio externo");
    }
    return response;
  }

  public String obtenerProvinciaConMasHechosDeCategoria(String categoria) {
    String response = webApiCallerService.get(metamapaServiceUrl + "/estadisticas/provincia-mas-hechos-categoria?categoria=" + categoria, String.class);
    if (response == null) {
      throw new RuntimeException("Error al obtener estadistica en el servicio externo");
    }
    return response;
  }

  public Integer obtenerHoraConMasHechosDeCategoria(String categoria) {
    Integer response = webApiCallerService.get(metamapaServiceUrl + "/estadisticas/hora-mas-hechos-categoria?categoria=" + categoria, Integer.class);
    if (response == null) {
      throw new RuntimeException("Error al obtener estadistica en el servicio externo");
    }
    return response;
  }

  public Long obtenerCantidadSolicitudesSpam() {
    Long response = webApiCallerService.get(metamapaServiceUrl + "/estadisticas/solicitudes-spam", Long.class);
    if (response == null) {
      throw new RuntimeException("Error al obtener estadistica en el servicio externo");
    }
    return response;
  }


  private String generarUrl(String handle, HechoFiltroDTO filtros) {
    Boolean curado = filtros.getCurado();
    Integer page = filtros.getPage();
    Integer size = filtros.getSize();

    String baseUrl = metamapaServiceUrl + "/colecciones/" + handle + "/hechos" + "?curado=" + curado.toString();
    StringBuilder url = new StringBuilder(baseUrl);

    // Agregar parámetros de paginación
    if (page != null) {
      url.append("&page=").append(page);
    }
    if (size != null) {
      url.append("&size=").append(size);
    }

    for (Field field : HechoFiltroDTO.class.getDeclaredFields()) {
      field.setAccessible(true);
      try {
        Object value = field.get(filtros);
        if (value != null) {
          url.append("&").append(field.getName()).append("=").append(value);
        }
      } catch (IllegalAccessException ignored) {
      }
    }
    return url.toString();
  }

  // Métodos auxiliares simples para JSON libre
  public Map<String, Object> getPublicJson(String path) {
    return webApiCallerService.getPublic(metamapaServiceUrl + path, Map.class);
  }

  public void postPublicJson(String path, Object body) {
    webApiCallerService.postPublic(metamapaServiceUrl + path, body, Void.class);
  }


  public List<FuenteOutputDTO> getTodasLasFuentes() {
    List<FuenteOutputDTO> response = webApiCallerService.getList(metamapaServiceUrl + "/fuentes", FuenteOutputDTO.class);
    return response != null ? response : List.of();
  }

  public void crearFuente(FuenteDTO fuenteDTO) {
    try {
      Map<String, String> body = new java.util.HashMap<>();
      body.put("tipo", fuenteDTO.getTipo());
      body.put("ruta", fuenteDTO.getRuta());
      if (fuenteDTO.getTitulo() != null && !fuenteDTO.getTitulo().isEmpty()) {
        body.put("titulo", fuenteDTO.getTitulo());
      }

      webApiCallerService.post(metamapaServiceUrl + "/fuentes", body, Void.class);

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

      MultiValueMap<String, HttpEntity<?>> body = new LinkedMultiValueMap<>();

      HttpHeaders fileHeaders = new HttpHeaders();
      fileHeaders.setContentDispositionFormData("archivo", archivo.getOriginalFilename());
      fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      body.add("archivo", new HttpEntity<>(archivo.getBytes(), fileHeaders));

      if (titulo != null && !titulo.isEmpty()) {
        HttpHeaders textHeaders = new HttpHeaders();
        textHeaders.setContentType(MediaType.TEXT_PLAIN);
        body.add("titulo", new HttpEntity<>(titulo, textHeaders));
      }

      webApiCallerService.post(metamapaServiceUrl, body, Void.class);

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

}
