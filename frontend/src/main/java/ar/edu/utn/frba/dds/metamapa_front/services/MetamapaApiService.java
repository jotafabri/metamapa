package ar.edu.utn.frba.dds.metamapa_front.services;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import ar.edu.utn.frba.dds.metamapa_front.dtos.*;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
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
  private final String authServiceUrl;
  private final String metamapaServiceUrl;

  @Autowired
  public MetamapaApiService(WebApiCallerService webApiCallerService, @Value("${auth.service.url}") String authServiceUrl, @Value("${colecciones.service.url}") String metamapaServiceUrl) {
    this.webClient = WebClient.builder().build();
    this.webApiCallerService = webApiCallerService;
    this.authServiceUrl = authServiceUrl;
    this.metamapaServiceUrl = metamapaServiceUrl;
  }

  public AuthResponseDTO login(String username, String password) {
    try {
      // Llamar al endpoint /api/auth/login que devuelve los JWT reales
      AuthResponseDTO authResponse = webClient
          .post()
          .uri(authServiceUrl + "/auth/login")
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
      throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
    }
  }

  public RolesPermisosDTO getRolesPermisos(String accessToken) {
    try {
      // Hacemos un GET al endpoint protegido /auth/user/roles-permisos
      Map<String, Object> response = webClient
              .get()
              .uri(metamapaServiceUrl + "/auth/user/roles-permisos")
              .header("Authorization", "Bearer " + accessToken)
              .retrieve()
              .bodyToMono(Map.class)
              .block();

      if (response == null) {
        throw new RuntimeException("No se pudo obtener la información del usuario.");
      }

      RolesPermisosDTO rolesPermisos = new RolesPermisosDTO();
      rolesPermisos.setUsername((String) response.get("email"));

      // Convertir rol de String a enum
      Object rolObj = response.get("rol");
      if (rolObj != null) {
        rolesPermisos.setRol(Rol.valueOf(rolObj.toString()));
      }

      // Convertir lista de permisos de String a enum
      Object permisosObj = response.get("permisos");
      if (permisosObj instanceof List<?>) {
        List<Permiso> permisos = ((List<?>) permisosObj)
                .stream()
                .map(Object::toString)
                .map(Permiso::valueOf)
                .toList();
        rolesPermisos.setPermisos(permisos);
      }

      return rolesPermisos;

    } catch (WebClientResponseException e) {
      log.error("Error HTTP al obtener roles y permisos: {}", e.getMessage());
      if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
        throw new RuntimeException("Token inválido o expirado. Por favor, inicie sesión nuevamente.");
      }
      throw new RuntimeException("Error al comunicarse con el backend: " + e.getMessage(), e);
    } catch (Exception e) {
      log.error("Error general al obtener roles y permisos", e);
      throw new RuntimeException("Error inesperado: " + e.getMessage(), e);
    }
  }




  public List<ColeccionDTO> getAllColecciones() {
    List<ColeccionDTO> response = webApiCallerService.getListPublic(metamapaServiceUrl + "/colecciones", ColeccionDTO.class);
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
    return response;
  }

  public ColeccionDTO actualizarColeccion(String handle, ColeccionDTO coleccionDTO) {
    ColeccionDTO response = webApiCallerService.patch(metamapaServiceUrl + "/colecciones/" + handle, coleccionDTO, ColeccionDTO.class);
    if (response == null) {
      throw new RuntimeException("Error al actualizar coleccion en el servicio externo");
    }
    return response;
  }

  public void eliminarColeccion(String handle) {
    webApiCallerService.delete(metamapaServiceUrl + "/colecciones/" + handle);
  }

  public HechoDTO getHechoById(Long id) {
    HechoDTO response = webApiCallerService.get(metamapaServiceUrl + "/hechos/" + id.toString(), HechoDTO.class);
    if (response == null) {
      throw new NotFoundException("Hecho", id.toString());
    }
    return response;
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
        response = webApiCallerService.postPublic(authServiceUrl + "/hechos", hechoDTO, HechoDTO.class);
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
    HechoDTO response = webApiCallerService.patch(metamapaServiceUrl + "/hechos/" + id.toString(), hechoDTO, HechoDTO.class);
    if (response == null) {
      throw new RuntimeException("Error al actualizar hecho en el servicio externo");
    }
    return response;
  }

  public void crearSolicitudEliminacion(SolicitudEliminacionDTO solicitudDTO) {
    webApiCallerService.postPublic(metamapaServiceUrl + "/solicitudes", solicitudDTO, SolicitudEliminacionDTO.class);
  }

  public void aceptarSolicitudEliminacion(Long id) {
    webApiCallerService.patch(metamapaServiceUrl + "/solicitudes/" + id.toString() + "/aceptar", null, null);
  }

  public void rechazarSolicitudEliminacion(Long id) {
    webApiCallerService.patch(metamapaServiceUrl + "/solicitudes/" + id.toString() + "/rechazar", null, null);
  }

  public void crearUsuario(RegistroRequest registroRequest) {
    webApiCallerService.postPublic(metamapaServiceUrl + "/auth/registro", registroRequest, RegistroRequest.class);
  }

  public AuthResponseDTO autenticar(LoginRequest loginRequest) {
    AuthResponseDTO response = webApiCallerService.postPublic(metamapaServiceUrl + "/auth/login", loginRequest, AuthResponseDTO.class);
    if (response == null) {
      throw new RuntimeException("Error al iniciar sesión en el servicio externo");
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
}
