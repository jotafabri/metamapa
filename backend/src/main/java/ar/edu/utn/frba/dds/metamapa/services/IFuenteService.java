package ar.edu.utn.frba.dds.metamapa.services;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;

public interface IFuenteService {

    List<Fuente> listarFuentes();

    Fuente mostrarFuente(Long id);

    Fuente crearFuente(FuenteInputDTO fuenteDTO);

    void eliminarFuente(Long id);

}