package ar.edu.utn.frba.dds.metamapa.models.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Categoria {
    String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}
