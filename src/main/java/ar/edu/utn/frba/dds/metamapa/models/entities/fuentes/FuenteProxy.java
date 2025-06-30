package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Origen;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class FuenteProxy extends Fuente {
  private final WebClient webClient;

  public FuenteProxy(String baseUrl) {
    this.webClient = WebClient.builder().baseUrl(baseUrl).build();
  }

  @Override
  public List<Hecho> getHechos() {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/hechos")
            .build())
        .retrieve()
        .bodyToFlux(HechoDTO.class)
        .map(response -> {
          Hecho hecho = new Hecho(
              response.getTitulo(),
              response.getDescripcion(),
              response.getCategoria(),
              response.getLatitud(),
              response.getLongitud(),
              response.getFechaAcontecimiento());
          hecho.setId(response.getId());
          hecho.setFechaCarga(response.getFechaCarga());
          return hecho;
        }).collectList().block();
  }
}
