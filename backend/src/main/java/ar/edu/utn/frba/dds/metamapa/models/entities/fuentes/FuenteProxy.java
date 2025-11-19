package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;

import java.util.List;
import java.util.Objects;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.ProxyInputDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@NoArgsConstructor
@Entity
@DiscriminatorValue("proxy")
public class FuenteProxy extends Fuente {
  @Transient
  private WebClient webClient;

  public FuenteProxy(String ruta) {
    this.ruta = ruta;
  }

  private WebClient getWebClient() {
    if (webClient == null) {
      webClient = WebClient.builder().baseUrl(ruta).build();
    }
    return webClient;
  }

  public List<Hecho> getHechos() {
    this.hechos = getWebClient().get()
        .uri(uriBuilder -> uriBuilder.path("/desastres").build())
        .retrieve()
        .bodyToMono(ProxyInputDTO.class)
        .map(inputDTO -> inputDTO.getData().stream()
            .map(HechoDTO::toHecho)
            .toList())
        .block().stream().filter(h -> h.getEliminado().equals(false)).toList();
    return this.hechos;
  }

  public Hecho getHechoFromId(Long id) {
    return this.hechos.stream().filter(h -> Objects.equals(h.getId(), id)).findFirst().orElse(
        getWebClient().get()
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
