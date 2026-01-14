package ar.edu.utn.frba.dds.metamapa_front.dtos.input;


import ar.edu.utn.frba.dds.metamapa_front.dtos.Rol;
import lombok.Data;

@Data
public class UserInputDTO {

    private Long id;
    private String nombre;
    private String email;
    private Rol rol;
}
