package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import lombok.Data;

@Data
public class ColeccionDTO {
  private String handle;
  private String titulo;
  private String descripcion;
  private String algoritmo;
  private Integer cantHechos;

  // Mapper
  public static ColeccionDTO fromColeccion(Coleccion coleccion) {
    var dto = new ColeccionDTO();
    dto.setHandle(coleccion.getHandle());
    dto.setTitulo(coleccion.getTitulo());
    dto.setDescripcion(coleccion.getDescripcion());
    dto.setCantHechos(coleccion.getHechos().size());
    return dto;
  }
}
