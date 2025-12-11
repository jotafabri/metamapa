package ar.edu.utn.frba.dds.metamapa.models.dtos.output;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import lombok.Data;

@Data
public class FuenteOutputDTO {
    private Long id;
    private String nombre;
    private String tipo;
    private String ruta;

    public static FuenteOutputDTO fromFuente(Fuente fuente) {
        var dto = new FuenteOutputDTO();
        dto.setId(fuente.getId());
        dto.setNombre(fuente.getNombre());
        dto.setTipo(fuente.getClass().getSimpleName());
        dto.setRuta(fuente.getRuta());
        return dto;
    }
}