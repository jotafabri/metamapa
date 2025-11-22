package ar.edu.utn.frba.dds.metamapa_front.services.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

@Service
public class GraphQlCallerService {
  private final WebClient webClient;
  private final String graphqlServiceUrl;
  private final ObjectMapper objectMapper;
  private final WebApiCallerService webApiCallerService;

  public GraphQlCallerService(
      @Value("${graphql.service.url}") String graphqlServiceUrl,
      WebApiCallerService webApiCallerService) {
    this.webClient = WebClient.builder().build();
    this.graphqlServiceUrl = graphqlServiceUrl;
    this.objectMapper = new ObjectMapper();
    this.webApiCallerService = webApiCallerService;
  }

  public <T> List<T> executeQueryForList(String query, Class<T> itemType) {
    return webApiCallerService.executeWithTokenRetry(accessToken -> {
      JsonNode listNode = getJsonNode(query, accessToken);
      if (listNode == null) return null;

      return objectMapper.convertValue(
          listNode,
          objectMapper.getTypeFactory().constructCollectionType(List.class, itemType)
      );
    });
  }

  public <T> T executeQuery(String query, Class<T> responseType) {
    return webApiCallerService.executeWithTokenRetry(accessToken -> {
      JsonNode objNode = getJsonNode(query, accessToken);
      if (objNode == null) return null;

      return objectMapper.convertValue(objNode, responseType);
    });
  }

  private JsonNode getJsonNode(String query, String accessToken) {
    JsonNode response = webClient.post()
        .uri(graphqlServiceUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + accessToken)
        .bodyValue(Map.of("query", query))
        .retrieve()
        .bodyToMono(JsonNode.class)
        .block();

    JsonNode dataNode = response.get("data");
    if (dataNode == null || !dataNode.fieldNames().hasNext()) {
      return null;
    }
    String firstKey = dataNode.fieldNames().next();
    return dataNode.get(firstKey);
  }
}
