package ar.edu.utn.frba.dds.metamapa.services;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.TipoAlgoritmo;

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
                                      Boolean soloConContribuyente);

  List<HechoDTO> getHechosCurados(String handle, Boolean curado);

  void crearDesdeDTO(ColeccionDTO coleccionDTO);

  ColeccionDTO mostrarColeccion(String handle);

  void actualizarColeccion(String handle, ColeccionDTO nuevaColeccionDTO);

  void eliminarColeccion(String handle);

  void cambiarAlgoritmo(String handle, TipoAlgoritmo tipo);
}
