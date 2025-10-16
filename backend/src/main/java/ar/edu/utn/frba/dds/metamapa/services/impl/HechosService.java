package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import ar.edu.utn.frba.dds.metamapa.exceptions.NotFoundException;
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
    return this.hechosRepository.findAllAceptados()
        .stream()
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
    Hecho hechoGuardado = hechosRepository.save(hecho);
    return HechoDTO.fromHecho(hechoGuardado);
  }

  @Override
  public HechoDTO getHechoById(Long id) {
    Hecho hecho = intentarRecuperarHecho(id);
    return HechoDTO.fromHecho(hecho);
  }

  @Override
  public HechoDTO actualizarHecho(Long id, HechoDTO hechoDTO) {
    Hecho hecho = intentarRecuperarHecho(id);

    hecho.setTitulo(hechoDTO.getTitulo());
    hecho.setDescripcion(hechoDTO.getDescripcion());
    hecho.setCategoria(hechoDTO.getCategoria());
    hecho.setLatitud(hechoDTO.getLatitud());
    hecho.setLongitud(hechoDTO.getLongitud());
    hecho.setFechaAcontecimiento(hechoDTO.getFechaAcontecimiento().atStartOfDay());

    Hecho hechoActualizado = hechosRepository.save(hecho);
    return HechoDTO.fromHecho(hechoActualizado);
  }

  @Override
  public void marcarEliminado(Long id) {
    Hecho hecho = intentarRecuperarHecho(id);
    hecho.eliminar();
    hechosRepository.save(hecho);
  }

  private Hecho intentarRecuperarHecho(Long id) {
    return hechosRepository.findById(id).orElseThrow(() -> new NotFoundException("Hecho", id.toString()));
  }
}
