package ar.edu.utn.frba.dds.metamapa.services;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;

public interface IColeccionService {
  List<ColeccionDTO> getAllColecciones();

  List<HechoDTO> getHechosByHandle(String handle,
                                   HechoFiltroDTO filtros,
                                   Boolean curado);

  List<HechoDTO> getHechosByHandleAdmin(String handle,
                                        HechoFiltroDTO filtros);

  void crearDesdeDTO(ColeccionDTO coleccionDTO);

  ColeccionDTO mostrarColeccion(String handle);

  void actualizarColeccion(String handle, ColeccionDTO nuevaColeccionDTO);

  void eliminarColeccion(String handle);

}
