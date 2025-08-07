package ar.edu.utn.frba.dds.metamapa.services;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;

public interface IColeccionService {
  List<ColeccionDTO> getAllColecciones();

  List<HechoDTO> getHechosByHandle(String handle,
                                   String categoria,
                                   String fecha_reporte_desde,
                                   String fecha_reporte_hasta,
                                   String fecha_acontecimiento_desde,
                                   String fecha_acontecimiento_hasta,
                                   String ubicacion,
                                   Boolean soloConMultimedia,
                                   Boolean soloConContribuyente,
                                   Boolean curado);

  List<HechoDTO> getHechosByHandleAdmin(String handle,
                                        String categoria,
                                        String fecha_reporte_desde,
                                        String fecha_reporte_hasta,
                                        String fecha_acontecimiento_desde,
                                        String fecha_acontecimiento_hasta,
                                        String ubicacion,
                                        Boolean soloConMultimedia,
                                        Boolean soloConContribuyente);

  void crearDesdeDTO(ColeccionDTO coleccionDTO);

  ColeccionDTO mostrarColeccion(String handle);

  void actualizarColeccion(String handle, ColeccionDTO nuevaColeccionDTO);

  void eliminarColeccion(String handle);

}
