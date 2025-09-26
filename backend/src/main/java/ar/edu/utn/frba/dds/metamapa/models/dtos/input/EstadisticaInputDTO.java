package ar.edu.utn.frba.dds.metamapa.models.dtos.input;

import lombok.Data;

@Data
public class EstadisticaInputDTO {
  private String coleccionHandle;
  private String categoria;
  private String provincia;
  private Integer hora;
}