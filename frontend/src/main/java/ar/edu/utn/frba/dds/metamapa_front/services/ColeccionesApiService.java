package ar.edu.utn.frba.dds.metamapa_front.services;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa_front.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ColeccionesApiService {

  private final WebClient webClient;
  private final WebApiCallerService webApiCallerService;
  private final String authServiceUrl;
  private final String coleccionesServiceUrl;

  @Autowired
  public ColeccionesApiService(
      WebApiCallerService webApiCallerService,
      @Value("${auth.service.url}") String authServiceUrl,
      @Value("${colecciones.service.url}") String coleccionesServiceUrl
  ) {
    this.webClient = WebClient.builder().build();
    this.webApiCallerService = webApiCallerService;
    this.authServiceUrl = authServiceUrl;
    this.coleccionesServiceUrl = coleccionesServiceUrl;
  }

  public List<ColeccionDTO> getAllColecciones() {
    List<ColeccionDTO> response = webApiCallerService.getList(
        coleccionesServiceUrl +
            "/colecciones",
        ColeccionDTO.class
    );
    return response != null ? response : List.of();
  }

  public List<HechoDTO> getHechosByHandle(String handle) {
    List<HechoDTO> response = webApiCallerService.getList(
        coleccionesServiceUrl +
            "/colecciones/" + handle + "/hechos",
        HechoDTO.class);
    return response != null ? response : List.of();
  }
}
