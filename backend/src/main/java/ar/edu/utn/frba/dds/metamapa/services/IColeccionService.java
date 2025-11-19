package ar.edu.utn.frba.dds.metamapa.services;

import java.util.List;
import java.util.Optional;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.DatosGeograficosDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;

public interface IColeccionService {
  List<ColeccionDTO> getAllColecciones();

  List<ColeccionDTO> getAllColecciones(Integer limit);

  List<HechoDTO> getHechosByHandle(String handle,
                                   HechoFiltroDTO filtros);

  List<HechoDTO> getHechosByHandleAdmin(String handle,
                                        HechoFiltroDTO filtros);

  DatosGeograficosDTO obtenerDatosGeograficos(String handle);

  ColeccionDTO crearDesdeDTO(ColeccionDTO coleccionDTO);

  Optional<ColeccionDTO> mostrarColeccion(String handle);

  ColeccionDTO actualizarColeccion(String handle, ColeccionDTO nuevaColeccionDTO);

  void eliminarColeccion(String handle);

  void agregarFuente(Long idColeccion, Long idFuente);

  void quitarFuente(Long idColeccion, Long idFuente);

}
