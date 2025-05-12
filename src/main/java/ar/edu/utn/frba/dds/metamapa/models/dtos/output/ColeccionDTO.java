package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import lombok.Data;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class ColeccionDTO {
    private String titulo;
    private String descripcion;
}
