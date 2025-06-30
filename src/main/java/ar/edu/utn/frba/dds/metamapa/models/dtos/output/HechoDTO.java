package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HechoDTO {
  private Long id; //
  private String titulo; //
  private String descripcion; //
  private String categoria; //
  private Double latitud; //
  private Double longitud; //
  private LocalDateTime fechaAcontecimiento; //
  private LocalDateTime fechaCarga; //

  private String multimediaUrl;
  private String contribuyenteNombre;

  public static HechoDTO fromHecho(Hecho hecho) {
    var dto = new HechoDTO();
    dto.setId(hecho.getId());
    dto.setTitulo(hecho.getTitulo());
    dto.setDescripcion(hecho.getDescripcion());
    dto.setCategoria(hecho.getCategoria());
    dto.setLatitud(hecho.getLatitud());
    dto.setLongitud(hecho.getLongitud());
    dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
    dto.setFechaCarga(hecho.getFechaCarga());

    if (hecho.getMultimedia() != null) {
      dto.setMultimediaUrl(hecho.getMultimedia().getUrlCompleta());
    }

    if (hecho.getContribuyente() != null) {
      dto.setContribuyenteNombre(hecho.getContribuyente().getNombre());
    }

    return dto;
  }
}
