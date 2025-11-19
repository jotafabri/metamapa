package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.metamapa.models.entities.Estadistica;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EstadisticaDTO {
  private String pregunta;
  private String parametro;
  private String respuesta;
  private LocalDateTime fechaGeneracion;

  public static EstadisticaDTO fromEstadistica(Estadistica estadistica) {
    var dto = new EstadisticaDTO();
    dto.setPregunta(estadistica.getPregunta());
    dto.setParametro(estadistica.getParametro());
    dto.setRespuesta(estadistica.getRespuesta());
    dto.setFechaGeneracion(estadistica.getFechaGeneracion());
    return dto;
  }
}