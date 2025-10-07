package ar.edu.utn.frba.dds.metamapa_front.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
  private LocalDateTime fechaAcontecimiento; //
  private LocalDateTime fechaCarga; //

  private List<MultipartFile> multimedia;
}
