package ar.edu.utn.frba.dds.metamapa.services;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoOutputDTO;

import java.util.List;

public interface IHechosService {
    public List<HechoOutputDTO> buscarTodos();
}
