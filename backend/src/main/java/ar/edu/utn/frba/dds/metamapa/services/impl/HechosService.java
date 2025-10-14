package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Origen;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IFuentesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HechosService implements IHechosService {
  private final Estado estadoPorDefecto;
  private final Long limiteDiasEdicion;
  private FuenteDinamica fuenteDinamica;

  @Autowired
  private IHechosRepository hechosRepository;

  @Autowired
  private IFuentesRepository fuentesRepository;

  private FuenteDinamica getFuenteDinamica() {
    // Buscamos la fuente dinámica
    if (this.fuenteDinamica == null) {
      Optional<FuenteDinamica> fuente = fuentesRepository.findFirstBy();
      if (fuente.isPresent()) {
        this.fuenteDinamica = fuente.get();
      } else {
        this.fuenteDinamica = new FuenteDinamica();
        fuentesRepository.save(this.fuenteDinamica);
      }
    }
    return this.fuenteDinamica;
  }

  public HechosService(
      @Value("${hecho.estado.por.defecto}") String estadoPorDefectoStr,
      @Value("${limite.dias.edicion}") Long limiteDiasEdicion
  ) {
    this.estadoPorDefecto = Estado.valueOf(estadoPorDefectoStr);
    this.limiteDiasEdicion = limiteDiasEdicion;

  }

  public Hecho crearHecho(String titulo, String descripcion, String categoria, Double latitud, Double longitud, LocalDateTime fechaAcontecimiento) {
    return Hecho.builder()
        .titulo(titulo)
        .descripcion(descripcion)
        .categoria(categoria)
        .latitud(latitud)
        .longitud(longitud)
        .fechaAcontecimiento(fechaAcontecimiento)
        .estado(estadoPorDefecto)
        .limiteDiasEdicion(limiteDiasEdicion)
        .origen(Origen.CONTRIBUYENTE)
        .fuente(getFuenteDinamica())
        .build();
  }

  @Override
  public List<HechoDTO> getHechosWithParams(HechoFiltroDTO filtros) {
    return this.hechosRepository.findAll()
        .stream()
        .filter(h -> h.getEstado() == Estado.ACEPTADA) // Solo los aseptados se van a exponer
        .filter(h -> filtros.getList().stream().allMatch(c -> c.cumple(h)))
        .map(HechoDTO::fromHecho)
        .toList();
  }

  @Override
  public HechoDTO crearHechoDesdeDTO(HechoDTO hechoDTO) {
    Hecho hecho = this.crearHecho(
        hechoDTO.getTitulo(),
        hechoDTO.getDescripcion(),
        hechoDTO.getCategoria(),
        hechoDTO.getLatitud(),
        hechoDTO.getLongitud(),
        hechoDTO.getFechaAcontecimiento().atStartOfDay()
    );
    if (hechoDTO.getMultimedia() != null && !hechoDTO.getMultimedia().isEmpty()) {
      hecho.agregarTodaMultimedia(hechoDTO.getMultimedia());
    }
    Hecho hechoGuardado = this.hechosRepository.save(hecho);
    return HechoDTO.fromHecho(hechoGuardado);
  }

  @Override
  public HechoDTO getHechoById(Long id) {
    Hecho hecho = this.hechosRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Hecho no encontrado con id: " + id));
    return HechoDTO.fromHecho(hecho);
  }

  @Override
  public HechoDTO actualizarHecho(Long id, HechoDTO hechoDTO) {
    Hecho hecho = this.hechosRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Hecho no encontrado con id: " + id));

    hecho.setTitulo(hechoDTO.getTitulo());
    hecho.setDescripcion(hechoDTO.getDescripcion());
    hecho.setCategoria(hechoDTO.getCategoria());
    hecho.setLatitud(hechoDTO.getLatitud());
    hecho.setLongitud(hechoDTO.getLongitud());
    hecho.setFechaAcontecimiento(hechoDTO.getFechaAcontecimiento().atStartOfDay());

    Hecho hechoActualizado = this.hechosRepository.save(hecho);
    return HechoDTO.fromHecho(hechoActualizado);
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
