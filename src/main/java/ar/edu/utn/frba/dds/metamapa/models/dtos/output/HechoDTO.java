package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import lombok.Data;

@Data
public class HechoDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private Float latitud;
  private Float longitud;
  private LocalDateTime fechaAcontecimiento;
  private LocalDateTime fechaCarga;

  public static HechoDTO fromHecho(Hecho hecho) {
    var dto = new HechoDTO();
    dto.setId(hecho.getId());
    dto.setTitulo(hecho.getTitulo());
    dto.setDescripcion(hecho.getDescripcion());
    dto.setCategoria(hecho.getCategoria());
    dto.setLatitud(hecho.getCoordenadas().getLatitud());
    dto.setLongitud(hecho.getCoordenadas().getLongitud());
    dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
    dto.setFechaCarga(hecho.getFechaCarga());
    return dto;
  }
}
