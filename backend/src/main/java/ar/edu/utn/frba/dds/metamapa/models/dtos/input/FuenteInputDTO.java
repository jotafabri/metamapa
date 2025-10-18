package ar.edu.utn.frba.dds.metamapa.models.dtos.input;

import lombok.Data;

@Data
public class FuenteInputDTO {
    private String tipo; // "estatica", "dinamica" o "proxy"
    private String ruta; // puede ser path local o URL según tipo
}