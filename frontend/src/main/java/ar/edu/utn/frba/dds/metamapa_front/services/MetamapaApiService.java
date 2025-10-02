package ar.edu.utn.frba.dds.metamapa_front.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import ar.edu.utn.frba.dds.metamapa_front.dtos.AuthResponseDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.RolesPermisosDTO;
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
  private final String coleccionesServiceUrl;

  @Autowired
  public MetamapaApiService(
      WebApiCallerService webApiCallerService,
      @Value("${auth.service.url}") String authServiceUrl,
      @Value("${colecciones.service.url}") String coleccionesServiceUrl
  ) {
    this.webClient = WebClient.builder().build();
    this.webApiCallerService = webApiCallerService;
    this.authServiceUrl = authServiceUrl;
    this.coleccionesServiceUrl = coleccionesServiceUrl;
  }

  public AuthResponseDTO login(String username, String password) {
    try {
      AuthResponseDTO response = webClient
          .post()
          .uri(authServiceUrl + "/auth")
          .bodyValue(Map.of(
              "username", username,
              "password", password
          ))
          .retrieve()
          .bodyToMono(AuthResponseDTO.class)
          .block();
      return response;
    } catch (WebClientResponseException e) {
      log.error(e.getMessage());
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        // Login fallido - credenciales incorrectas
        return null;
      }
      // Otros errores HTTP
      throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
    }
  }
  public RolesPermisosDTO getRolesPermisos(String accessToken) {
    try {
      RolesPermisosDTO response = webApiCallerService.getWithAuth(
          authServiceUrl + "/auth/user/roles-permisos",
          accessToken,
          RolesPermisosDTO.class
      );
      return response;
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException("Error al obtener roles y permisos: " + e.getMessage(), e);
    }
  }

  public List<ColeccionDTO> getAllColecciones() {
    List<ColeccionDTO> response = webApiCallerService.getListPublic(
        coleccionesServiceUrl,
        ColeccionDTO.class
    );
    return response != null ? response : List.of();
  }

  public List<HechoDTO> getHechosByHandle(String handle, HechoFiltroDTO filtros, Boolean curado) {
    List<HechoDTO> response = webApiCallerService.getListPublic(this.generarUrl(handle, filtros, curado), HechoDTO.class);
    return response != null ? response : List.of();
  }

  public ColeccionDTO getColeccionByHandle(String handle) {
    ColeccionDTO response = webApiCallerService.get(coleccionesServiceUrl + handle, ColeccionDTO.class);
    if (response == null) {
      throw new NotFoundException("Coleccion", handle);
    }
    return response;
  }

  public ColeccionDTO crearColeccion(ColeccionDTO coleccionDTO) {
    ColeccionDTO response = webApiCallerService.post(coleccionesServiceUrl, coleccionDTO, ColeccionDTO.class);
    if (response == null) {
      throw new RuntimeException("Error al crear coleccion en el servicio externo");
    }
    return response;
  }

  public ColeccionDTO actualizarColeccion(String handle, ColeccionDTO coleccionDTO) {
    ColeccionDTO response = webApiCallerService.patch(coleccionesServiceUrl + handle, coleccionDTO, ColeccionDTO.class);
    if (response == null) {
      throw new RuntimeException("Error al actualizar coleccion en el servicio externo");
    }
    return response;
  }

  public void eliminarColeccion(String handle) {
    webApiCallerService.delete(coleccionesServiceUrl + handle);
  }

  private String generarUrl(String handle, HechoFiltroDTO filtros, Boolean curado) {
    String baseUrl = coleccionesServiceUrl + handle + "/hechos" + "?curado=" + curado.toString();
    StringBuilder url = new StringBuilder(baseUrl);

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
