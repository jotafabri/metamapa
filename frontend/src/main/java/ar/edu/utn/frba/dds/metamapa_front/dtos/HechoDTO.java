package ar.edu.utn.frba.dds.metamapa_front.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HechoDTO {
  private Long id; //
  private String titulo; //
  private String descripcion; //
  private String categoria; //
  private Double latitud; //
  private Double longitud; //
  private LocalDate fechaAcontecimiento; //
  private LocalDateTime fechaCarga; //
  private Long contribuyenteId;

  private List<String> multimedia;
}
