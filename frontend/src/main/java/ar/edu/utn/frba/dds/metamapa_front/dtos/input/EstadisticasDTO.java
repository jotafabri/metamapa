package ar.edu.utn.frba.dds.metamapa_front.dtos.input;

import lombok.Data;

@Data
public class EstadisticasDTO {

    private String categoriaMasHechos;
    private Long solicitudesSpam;
    private String provinciaMasHechos;
    private Integer horaMasHechos;
}