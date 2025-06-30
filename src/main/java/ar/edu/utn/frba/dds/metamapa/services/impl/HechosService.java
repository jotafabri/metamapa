package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.EstadoHecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Origen;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.ListaDeFiltros;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HechosService implements IHechosService {
    @Autowired
    private IHechosRepository hechosRepository;


    private final Estado estadoPorDefecto;
    private final Long limiteDiasEdicion;

    public HechosService(
            @Value("${hecho.estado.por.defecto}") String estadoPorDefectoStr,
            @Value("${limite.dias.edicion}") Long limiteDiasEdicion
    ) {
        this.estadoPorDefecto = Estado.valueOf(estadoPorDefectoStr);
        this.limiteDiasEdicion = limiteDiasEdicion;
    }

    public Hecho crearHecho(String titulo, String descripcion,String categoria ,Double latitud, Double longitud, LocalDateTime fechaAcontecimiento) {
        Hecho hecho = new Hecho(titulo, descripcion,categoria, latitud, longitud, fechaAcontecimiento);
        hecho.setEstado(estadoPorDefecto);
        hecho.setLimiteDiasEdicion(limiteDiasEdicion);
        return hecho;
    }

    public Hecho crearHecho(String titulo, String descripcion, String categoria , Double latitud, Double longitud, LocalDateTime fechaAcontecimiento, Origen origen) {
        Hecho hecho = new Hecho(titulo, descripcion,categoria, latitud, longitud, fechaAcontecimiento, origen);
        hecho.setEstado(estadoPorDefecto);
        hecho.setLimiteDiasEdicion(limiteDiasEdicion);
        return hecho;
    }




    @Override
  public List<HechoDTO> getHechosWithParams(String categoria,
                                            String fecha_reporte_desde,
                                            String fecha_reporte_hasta,
                                            String fecha_acontecimiento_desde,
                                            String fecha_acontecimiento_hasta,
                                            String ubicacion,
                                            Boolean soloConMultimedia,
                                            Boolean soloConContribuyente) {
    var filtro = new ListaDeFiltros().getListFromParams(categoria,
        fecha_reporte_desde,
        fecha_reporte_hasta,
        fecha_acontecimiento_desde,
        fecha_acontecimiento_hasta,
        ubicacion,
        soloConMultimedia,
        soloConContribuyente);

    return this.hechosRepository.findAll()
        .stream()
            .filter(h -> h.getEstado() == Estado.ACEPTADA) // Solo los aseptados se van a exponer
        .filter(h -> filtro.stream().allMatch(c -> c.cumple(h)))
        .map(HechoDTO::fromHecho)
        .toList();
  }

/*
//dinamicos

  @Override
  public List<HechoOutputDTO> buscarTodos(String categoria,
                                                   String fecha_reporte_desde,
                                                   String fecha_reporte_hasta,
                                                   String fecha_acontecimiento_desde,
                                                   String fecha_acontecimiento_hasta,
                                                   String ubicacion
                                             ) {
      var criterios = new ListaDeFiltros().getListFromParams(
              categoria,
              fecha_reporte_desde,
              fecha_reporte_hasta,
              fecha_acontecimiento_desde,
              fecha_acontecimiento_hasta,
              ubicacion,
              null,
              null
      );

      return hechosRepository.findAll().stream()
              .filter(h -> h.getEstado() == Estado.ACEPTADA) // Solo los aseptados se van a exponer
              .filter(h -> criterios.stream().allMatch(c -> c.cumple(h)))
              .map(HechoOutputDTO::fromHechoDinamico)
              .toList();
  }

*/


}
