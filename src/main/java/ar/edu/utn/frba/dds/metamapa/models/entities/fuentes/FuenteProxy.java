package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;

import java.util.List;
import java.util.Objects;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.ProxyInputDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@NoArgsConstructor
@Entity
@Table(name = "fuente_proxy")
public class FuenteProxy extends Fuente {


  @Transient
  private WebClient webClient;

  @Column(name = "base_url", nullable = false)
  private String baseUrl;

  // La baseUrl es seteada al momento de crear la fuente y agregarla a una coleccion
  public FuenteProxy(String baseUrl) {
    this.baseUrl = baseUrl;
    this.webClient = WebClient.builder().baseUrl(baseUrl).build();
  }

  public List<Hecho> getHechos() {
    this.hechos = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/desastres")
            .build())
        .retrieve()
        .bodyToMono(ProxyInputDTO.class)
        .map(inputDTO -> inputDTO.getData().stream()
            .map(HechoDTO::toHecho)
            .toList()
        )
        .block().stream().filter(h -> h.getEliminado().equals(false)).toList();
      return this.hechos;
  }

  public Hecho getHechoFromId(Long id) {
    return this.hechos.stream().filter(h -> Objects.equals(h.getId(), id)).findFirst().orElse(
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
