package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ColeccionDTO {
  private String handle;
  private String titulo;
  private String descripcion;
  private String algoritmo;
  private Integer cantHechos;
  private List<FuenteOutputDTO> fuentes;
  private HechoFiltroDTO criterios;

  // Mapper
  public static ColeccionDTO fromColeccion(Coleccion coleccion) {
    var dto = new ColeccionDTO();
    dto.setHandle(coleccion.getHandle());
    dto.setTitulo(coleccion.getTitulo());
    dto.setDescripcion(coleccion.getDescripcion());
    dto.setAlgoritmo(
            coleccion.getAlgoritmoDeConsenso() != null
                    ? coleccion.getAlgoritmoDeConsenso().getNombre().toUpperCase()
                    : "-"
    );
    dto.setCantHechos(
            (int) coleccion.getHechos().stream()
                    .filter(h -> h.getEstado() == Estado.ACEPTADA)
                    .filter(h -> Boolean.FALSE.equals(h.getEliminado()))
                    .count()
    );
    dto.setFuentes(
            coleccion.getFuentes().stream()
                    .map(FuenteOutputDTO::fromFuente)
                    .collect(Collectors.toList())
    ); // ✅ mapeamos las fuentes
    dto.setCriterios(
            HechoFiltroDTO.fromCriterios(coleccion.getCriterios())
    );

    return dto;
  }
}
