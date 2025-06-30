package ar.edu.utn.frba.dds.metamapa.services;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoOutputDTO;

public interface IHechosService {
  List<HechoDTO> getHechosWithParams(
          String categoria,
          String fecha_reporte_desde,
          String fecha_reporte_hasta,
          String fecha_acontecimiento_desde,
          String fecha_acontecimiento_hasta,
          String ubicacion,
          Boolean soloConMultimedia,
          Boolean soloConContribuyente);

  /*public List<HechoOutputDTO> buscarTodos(
          String categoria,
          String fecha_reporte_desde,
          String fecha_reporte_hasta,
          String fecha_acontecimiento_desde,
          String fecha_acontecimiento_hasta,
          String ubicacion);*/
}
