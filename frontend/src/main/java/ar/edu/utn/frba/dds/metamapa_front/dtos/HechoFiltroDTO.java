package ar.edu.utn.frba.dds.metamapa_front.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HechoFiltroDTO {
  private String titulo;
  private String categoria;
  private String fechaReporteDesde;
  private String fechaReporteHasta;
  private String fechaAcontecimientoDesde;
  private String fechaAcontecimientoHasta;
  private String pais;
  private String provincia;
  private String localidad;
  private Boolean soloConMultimedia;
  private Boolean soloConContribuyente;
  private Boolean curado = false;
  private Integer page = 0;
  private Integer size = 10;
}
