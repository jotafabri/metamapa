package ar.edu.utn.frba.dds.metamapa.models.entities;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
              response.getFechaAcontecimiento(),
              Origen.PROXY);
          hecho.setId(response.getId());
          hecho.setFechaCarga(response.getFechaCarga());
          return hecho;
        }).collectList().block();
  }
}
