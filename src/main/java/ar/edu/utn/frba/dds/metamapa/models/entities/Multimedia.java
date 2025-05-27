package ar.edu.utn.frba.dds.metamapa.models.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class Multimedia {

  private String baseUrl;
  private String url;

  public Multimedia(String baseUrl, String url) {
    this.baseUrl = baseUrl;
    this.url = url;
  }

  public Multimedia() {
  }

  public String getUrlCompleta() {
    if (baseUrl == null) {
      return url != null ? url : "";
    }
    return baseUrl + (url != null ? url : "");
  }

}
