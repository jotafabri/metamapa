package ar.edu.utn.frba.dds.metamapa.models.dtos.output;


import lombok.Data;

@Data
public class EstadisticasDTO {

    private String categoriaMasHechos;
    private Long solicitudesSpam;
    private String provinciaMasHechos;
    private Integer horaMasHechos;
}