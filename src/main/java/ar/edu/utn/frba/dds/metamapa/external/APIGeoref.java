package ar.edu.utn.frba.dds.metamapa.external;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.ProxyInputDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.reactive.function.client.WebClient;

public class APIGeoref {
    private WebClient webClient;
    private final String baseUrl;

    public APIGeoref(String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public String getNombreProvincia(Double latitud, Double longitud) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/ubicacion")
                            .queryParam("lat", latitud)
                            .queryParam("lon", longitud)
                            .queryParam("aplanar", true)
                            .queryParam("campos", "basico")
                            .build())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .map(json -> json.path("ubicacion")
                            .path("provincia_nombre")
                            .asText())
                    .block();

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener provincia: " + e.getMessage(), e);
        }
    }
}
