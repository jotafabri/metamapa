package ar.edu.utn.frba.dds.metamapa_front.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionDTO {
  private String handle;
  private String titulo;
  private String descripcion;
  private String algoritmo;
  private Integer cantHechos;
  private HechoFiltroDTO criterios;
  private List<FuenteOutputDTO> fuentes;
  private List<Long> fuentesIds;
}
