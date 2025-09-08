package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Estadistica {
  private Long id;
  private String pregunta;
  private String parametro;
  private String respuesta;
  
  @Builder.Default
  private LocalDateTime fechaGeneracion = LocalDateTime.now();
}