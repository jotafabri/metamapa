package ar.edu.utn.frba.dds.metamapa_front.services.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

  public <T> T executePublicQuery(String query, Class<T> responseType) {
    JsonNode objNode = getJsonNode(query, null);
    if (objNode == null) return null;
    return objectMapper.convertValue(objNode, responseType);
  }

  private JsonNode getJsonNode(String query, String accessToken) {
    Supplier<JsonNode> response = () -> {
      var post = webClient.post()
          .uri(graphqlServiceUrl)
          .contentType(MediaType.APPLICATION_JSON);
      if (accessToken != null) {
        post = post.header("Authorization", "Bearer " + accessToken);
      }
      return post.bodyValue(Map.of("query", query))
          .retrieve()
          .bodyToMono(JsonNode.class)
          .block();
    };

    JsonNode getResponse = response.get();
    JsonNode dataNode = getResponse != null ? getResponse.get("data") : null;
    if (dataNode == null || !dataNode.fieldNames().hasNext()) {
      return null;
    }
    String firstKey = dataNode.fieldNames().next();
    return dataNode.get(firstKey);
  }
}
