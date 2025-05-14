package ar.edu.utn.frba.dds.metamapa.services;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;

public interface IColeccionService {
  List<ColeccionDTO> getAllColecciones();
  List<HechoDTO> getHechosById(Long id,
                                      String categoria,
                                      String fecha_reporte_desde,
                                      String fecha_reporte_hasta,
                                      String fecha_acontecimiento_desde,
                                      String fecha_acontecimiento_hasta,
                                      String ubicacion);
}
