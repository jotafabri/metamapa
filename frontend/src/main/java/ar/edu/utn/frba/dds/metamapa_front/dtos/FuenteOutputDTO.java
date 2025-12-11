package ar.edu.utn.frba.dds.metamapa_front.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuenteOutputDTO {

    private Long id;
    private String nombre;
    private String tipo;
    private String ruta;

}
