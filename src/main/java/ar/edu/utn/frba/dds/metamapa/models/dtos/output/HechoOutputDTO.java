package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class HechoOutputDTO {

  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private String multimediaURL; //
  private Double latitudCoordenada;
  private Double longitudCoordenada;
  private LocalDateTime fechaAcontecimiento;
  private LocalDateTime fechaCarga;

  public HechoOutputDTO() {
  }

  public static HechoOutputDTO fromHechoDinamico(Hecho hecho) {
    HechoOutputDTO dto = new HechoOutputDTO();
    dto.setId(hecho.getId());
    dto.setTitulo(hecho.getTitulo());
    dto.setDescripcion(hecho.getDescripcion());
    dto.setCategoria(hecho.getCategoria());
    dto.setLongitudCoordenada(hecho.getLongitud());
    dto.setLatitudCoordenada(hecho.getLatitud());
    dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
    dto.setFechaCarga(hecho.getFechaCarga());
    dto.setMultimediaURL(hecho.getMultimedia() != null ? hecho.getMultimedia().getUrlCompleta() : null);
    return dto;
  }
}
