package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class HechoOutputDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String multimediaURL;
    private Double latitudCoordenada;
    private Double longitudCoordenada;
    private LocalDateTime fechaAcontecimiento;

    public HechoOutputDTO() {
    }

    public HechoOutputDTO(Hecho hecho) {
        this.titulo = hecho.getTitulo();
        this.descripcion = hecho.getDescripcion();
        this.multimediaURL = hecho.getMultimedia() != null ? hecho.getMultimedia().getUrlCompleta() : null;
    }
}
