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
  public List<HechoDTO> getHechosById(Long id,
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

    return coleccionesRepository.findById(id)
        .navegar(filtro)
        .stream()
        .map(HechoDTO::fromHecho)
        .toList();
  }

  private String generarHandleUnico(String baseTitulo) {
    String base = baseTitulo.toLowerCase().replaceAll("[^a-z0-9]", "");
    String candidato = base;
    int i = 1;
    List<String> handlesExistentes = this.coleccionesRepository.findAll().stream().map(c -> c.getHandle()).toList();
    while (handlesExistentes.contains(candidato)) {
      candidato = base + i;
      i++;
    }
    return candidato;
  }

  @Override
  public void crearDesdeDTO(ColeccionDTO coleccionDTO) {
    String handle = generarHandleUnico(coleccionDTO.getTitulo());
    coleccionesRepository.save(new Coleccion(coleccionDTO.getTitulo(), coleccionDTO.getDescripcion(), handle, null, null));
  }
}
