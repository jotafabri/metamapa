package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;

import java.util.List;
import java.util.Objects;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.ProxyInputDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import org.springframework.web.reactive.function.client.WebClient;

public class FuenteProxy extends Fuente {
  private final WebClient webClient;

  // La baseUrl es seteada al momento de crear la fuente y agregarla a una coleccion
  public FuenteProxy(String baseUrl) {
    this.webClient = WebClient.builder().baseUrl(baseUrl).build();
  }

  @Override
  public List<Hecho> getHechos() {
    hechos = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/desastres")
            .build())
        .retrieve()
        .bodyToMono(ProxyInputDTO.class)
        .map(inputDTO -> inputDTO.getData().stream()
            .map(HechoDTO::toHecho)
            .toList()
        )
        .block();
    return hechos;
  }

  @Override
  public Hecho getHechoFromId(Long id) {
    return hechos.stream().filter(h -> Objects.equals(h.getId(), id)).findFirst().orElse(
        webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/desastres/{id}")
                .build(id)
            )
            .retrieve()
            .bodyToMono(HechoDTO.class)
            .map(HechoDTO::toHecho)
            .block()
    );
  }
}
