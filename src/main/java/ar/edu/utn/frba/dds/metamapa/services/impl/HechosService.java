package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.EstadoHecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.ListaDeFiltros;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HechosService implements IHechosService {
    @Autowired
    private IHechosRepository hechosRepository;


    @Override
  public List<HechoDTO> getHechosWithParams(String categoria,
                                            String fecha_reporte_desde,
                                            String fecha_reporte_hasta,
                                            String fecha_acontecimiento_desde,
                                            String fecha_acontecimiento_hasta,
                                            String ubicacion) {
    var filtro = new ListaDeFiltros().getListFromParams(categoria,
        fecha_reporte_desde,
        fecha_reporte_hasta,
        fecha_acontecimiento_desde,
        fecha_acontecimiento_hasta,
        ubicacion);

    return this.hechosRepository.findAll()
        .stream()
        .filter(h -> filtro.stream().allMatch(c -> c.cumple(h)))
        .map(HechoDTO::fromHecho)
        .toList();
  }


//dinamicos
  @Override
  public List<HechoOutputDTO> buscarTodos(String categoria,
                                                   String fecha_reporte_desde,
                                                   String fecha_reporte_hasta,
                                                   String fecha_acontecimiento_desde,
                                                   String fecha_acontecimiento_hasta,
                                                   String ubicacion) {
      var criterios = new ListaDeFiltros().getListFromParams(
              categoria,
              fecha_reporte_desde,
              fecha_reporte_hasta,
              fecha_acontecimiento_desde,
              fecha_acontecimiento_hasta,
              ubicacion
      );

      return hechosRepository.findAll().stream()
              .filter(h -> h.getEstado() == Estado.ACEPTADA) // Solo los aseptados se van a exponer
              .filter(h -> criterios.stream().allMatch(c -> c.cumple(h)))
              .map(HechoOutputDTO::fromHechoDinamico)
              .toList();
  }




}
