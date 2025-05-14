package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.ListaDeCriterios;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.services.IColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColeccionService implements IColeccionService {

  @Autowired
  private IColeccionesRepository coleccionesRepository;

  @Override
  public List<ColeccionDTO> getAllColecciones() {
    return this.coleccionesRepository.findAll()
        .stream()
        .map(ColeccionDTO::fromColeccion)
        .toList();
  }

  @Override
  public List<HechoDTO> getHechosByHandle(String handle,
                                          String categoria,
                                          String fecha_reporte_desde,
                                          String fecha_reporte_hasta,
                                          String fecha_acontecimiento_desde,
                                          String fecha_acontecimiento_hasta,
                                          String ubicacion) {
    var filtro = new ListaDeCriterios().getListFromParams(categoria,
        fecha_reporte_desde,
        fecha_reporte_hasta,
        fecha_acontecimiento_desde,
        fecha_acontecimiento_hasta,
        ubicacion);

    return coleccionesRepository.findByHandle(handle)
        .navegar(filtro)
        .stream()
        .map(HechoDTO::fromHecho)
        .toList();
  }

  @Override
  public void crearDesdeDTO(ColeccionDTO coleccionDTO) {
    coleccionesRepository.save(new Coleccion(coleccionDTO.getTitulo(), coleccionDTO.getDescripcion(), null));
  }
}
