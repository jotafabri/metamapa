package ar.edu.utn.frba.dds.metamapa.models.dtos.input;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import lombok.Data;

// Obtenida de la API expuesta de metamapa para la fuenteproxy
@Data
public class ProxyInputDTO {
  private List<HechoDTO> data;

  public List<HechoDTO> getData() {
    return data;
  }
}
