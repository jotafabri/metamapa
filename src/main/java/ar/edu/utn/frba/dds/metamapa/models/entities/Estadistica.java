package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "estadistica")
public class Estadistica extends Persistente {
  @Column(name = "pregunta")
  private String pregunta;

  @Column(name = "parametro")
  private String parametro;

  @Column(name = "respuesta")
  private String respuesta;
  
  @Builder.Default
  private LocalDateTime fechaGeneracion = LocalDateTime.now();
}