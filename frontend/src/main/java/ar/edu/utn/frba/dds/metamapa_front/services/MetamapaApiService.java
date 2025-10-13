package ar.edu.utn.frba.dds.metamapa_front.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import ar.edu.utn.frba.dds.metamapa_front.dtos.AuthResponseDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.RolesPermisosDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa_front.services.internal.WebApiCallerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
  public MetamapaApiService(
      WebApiCallerService webApiCallerService,
      @Value("${auth.service.url}") String authServiceUrl,
      @Value("${colecciones.service.url}") String metamapaServiceUrl
  ) {
    this.webClient = WebClient.builder().build();
    this.webApiCallerService = webApiCallerService;
    this.authServiceUrl = authServiceUrl;
    this.metamapaServiceUrl = metamapaServiceUrl;
  }

  public AuthResponseDTO login(String username, String password) {
    try {
      // Llamamos al nuevo endpoint /api/auth/login
      Map<String, Object> response = webClient
          .post()
          .uri(metamapaServiceUrl + "/auth/login")
          .bodyValue(Map.of(
              "email", username,  // Usamos email en lugar de username
              "password", password
          ))
          .retrieve()
          .bodyToMono(Map.class)
          .block();

      if (response == null) {
        return null;
      }

      // Convertir la respuesta simple a AuthResponseDTO
      // Como no usamos JWT, creamos tokens simples basados en el email
      AuthResponseDTO authResponse = new AuthResponseDTO();
      authResponse.setAccessToken("simple-token-" + username);
      authResponse.setRefreshToken("refresh-token-" + username);

      return authResponse;
    } catch (WebClientResponseException e) {
      log.error(e.getMessage());
      if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.NOT_FOUND) {
        // Login fallido - credenciales incorrectas
        return null;
      }
      // Otros errores HTTP
      throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
    }
  }
  public RolesPermisosDTO getRolesPermisos(String email) {
    try {
      // Llamar al nuevo endpoint /api/auth/user con el email
      Map<String, Object> response = webClient
          .post()
          .uri(metamapaServiceUrl + "/api/auth/user")
          .bodyValue(Map.of("email", email))
          .retrieve()
          .bodyToMono(Map.class)
          .block();

      if (response == null) {
        throw new RuntimeException("Usuario no encontrado");
      }

      // Convertir la respuesta a RolesPermisosDTO
      RolesPermisosDTO rolesPermisos = new RolesPermisosDTO();
      // El rol viene como string del backend (ej: "ADMIN" o "USER")
      String rolStr = (String) response.get("rol");
      // Aquí asumimos que RolesPermisosDTO tiene un método setRol que acepta un enum
      // Por ahora devolvemos un DTO simple sin permisos
      rolesPermisos.setRol(rolStr);
      rolesPermisos.setPermisos(List.of()); // Sin permisos por ahora

      return rolesPermisos;
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException("Error al obtener roles y permisos: " + e.getMessage(), e);
    }
  }

  public List<ColeccionDTO> getAllColecciones() {
    List<ColeccionDTO> response = webApiCallerService.getListPublic(
        metamapaServiceUrl + "/colecciones",
        ColeccionDTO.class
    );
    return response != null ? response : List.of();
  }

  public List<HechoDTO> getHechosByHandle(String handle, HechoFiltroDTO filtros) {
    List<HechoDTO> response = webApiCallerService.getListPublic(this.generarUrl(handle, filtros), HechoDTO.class);
    return response != null ? response : List.of();
  }

  public List<String> getCategoriasByHandle(String handle) {
    List<String> response = webApiCallerService.getListPublic(metamapaServiceUrl + "/colecciones/" + handle + "/categorias", String.class);
    return response != null ? response : List.of();
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

  public HechoDTO crearHecho(HechoDTO hechoDTO) {
    HechoDTO response = webApiCallerService.postPublic(metamapaServiceUrl + "/hechos", hechoDTO, HechoDTO.class);
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

public SolicitudEliminacionDTO crearSolicitudEliminacion(SolicitudEliminacionDTO solicitudDTO) {
    SolicitudEliminacionDTO response = webApiCallerService.post(metamapaServiceUrl + "/solicitudes", solicitudDTO, SolicitudEliminacionDTO.class);
    if (response == null) {
      throw new RuntimeException("Error al crear solicitud de eliminación en el servicio externo");
    }
    return response;
  }

  public void aceptarSolicitudEliminacion(Long id) {
    webApiCallerService.patch(metamapaServiceUrl + "/solicitudes/" + id.toString() + "/aceptar", null, null );
  }

  public void rechazarSolicitudEliminacion(Long id) {
    webApiCallerService.patch(metamapaServiceUrl + "/solicitudes/" + id.toString() + "/rechazar", null, null );
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
