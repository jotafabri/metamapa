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
    public List<Hecho> getListaHechos() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/hechos")
                        .build())
                .retrieve()
                .bodyToFlux(HechoDTO.class)
                .map(response -> {
                    Hecho hecho = new Hecho();
                    //TODO aplicar constructor
                    hecho.setId(response.getId());
                    hecho.setTitulo(response.getTitulo());
                    hecho.setDescripcion(response.getDescripcion());
                    hecho.setCategoria(response.getCategoria());
                    hecho.setLatitud(response.getLatitud());
                    hecho.setLongitud(response.getLongitud());
                    hecho.setFechaAcontecimiento(response.getFechaAcontecimiento());
                    hecho.setFechaCarga(response.getFechaCarga());
                    return hecho;
                }).collectList().block();
    }
}
