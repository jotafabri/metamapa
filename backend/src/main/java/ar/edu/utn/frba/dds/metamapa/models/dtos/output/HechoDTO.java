package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import java.time.LocalDateTime;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
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

  private List<String> multimedia;
  private Long contribuyenteId;

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
    dto.setMultimedia(hecho.getMultimedia());

    if (hecho.getContribuyente() != null) {
      dto.setContribuyenteId(hecho.getContribuyente().getId());
    }

    return dto;
  }

  public Hecho toHecho() {
    return Hecho.builder()
        .titulo(this.titulo)
        .descripcion(this.descripcion)
        .categoria(this.categoria)
        .latitud(this.latitud)
        .longitud(this.longitud)
        .fechaAcontecimiento(this.fechaAcontecimiento)
        .fechaCarga(this.fechaCarga)
        .build();
  }
}
