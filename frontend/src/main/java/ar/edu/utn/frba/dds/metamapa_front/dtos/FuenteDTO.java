package ar.edu.utn.frba.dds.metamapa_front.dtos;

import lombok.Data;

@Data
public class FuenteDTO {
  private Long id;
  private String tipo; // "estatica", "dinamica" o "proxy"
  private String ruta; // puede ser path local o URL según tipo
  private String titulo; // título descriptivo de la fuente
}
