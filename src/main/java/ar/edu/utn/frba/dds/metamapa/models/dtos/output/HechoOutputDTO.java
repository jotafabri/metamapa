package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class HechoOutputDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String multimediaURL; //
    private Double latitudCoordenada;
    private Double longitudCoordenada;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;

    public HechoOutputDTO() {
    }
}
