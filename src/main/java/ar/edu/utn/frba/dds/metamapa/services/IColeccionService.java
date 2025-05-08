package ar.edu.utn.frba.dds.metamapa.services;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;

public interface IColeccionService {
  public List<ColeccionDTO> getAllColecciones();
  public List<HechoDTO> getHechosById(Long id);
}
