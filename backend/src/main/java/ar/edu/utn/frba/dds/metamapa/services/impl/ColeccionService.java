package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.metamapa.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa.models.dtos.input.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.DatosGeograficosDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.EstrategiaConsenso;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.TipoAlgoritmo;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.Filtro;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IFuentesRepository;
import ar.edu.utn.frba.dds.metamapa.services.IColeccionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColeccionService implements IColeccionService {

  @Autowired
  private IColeccionesRepository coleccionesRepository;

  @Autowired
  private IFuentesRepository fuentesRepository;

  // ADMIN:Operacion C(R)UD
  @Override
  public List<ColeccionDTO> getAllColecciones(Integer limit) {
    return this.coleccionesRepository.findAllLimit(limit)
        .stream()
        .map(ColeccionDTO::fromColeccion)
        .toList();
  }

  @Override
  public List<ColeccionDTO> getAllColecciones() {
    return this.coleccionesRepository.findAll()
        .stream()
        .map(ColeccionDTO::fromColeccion)
        .toList();
  }

  // USUARIO: Navegacion filtrada sobre una coleccion , no muestra los RECHAZADOS
  // y PENDIENTES.
  @Override
  public List<HechoDTO> getHechosByHandle(String handle,
      HechoFiltroDTO filtros) {
    Coleccion coleccion = intentarRecuperarColeccion(handle);
    return coleccion
        .navegar(filtros.getList(), filtros.getCurado())
        .stream()
        .filter(h -> h.getEstado().equals(Estado.ACEPTADA))
        .map(HechoDTO::fromHecho)
        .toList();
  }

  // ADMIN: Muestra todos los hechos de la coleccion , los que no fueron ACEPTADOS
  // tambien.
  @Override
  public List<HechoDTO> getHechosByHandleAdmin(String handle,
      HechoFiltroDTO filtros) {
    Coleccion coleccion = intentarRecuperarColeccion(handle);
    return coleccionesRepository.findColeccionByHandle(coleccion.getHandle())
        .navegar(filtros.getList(), false)
        .stream()
        .map(HechoDTO::fromHecho)
        .toList();
  }

  public DatosGeograficosDTO obtenerDatosGeograficos(String handle) {
    Coleccion coleccion = intentarRecuperarColeccion(handle);
    List<Object[]> raw = coleccionesRepository.findDatosRawByHandle(coleccion.getHandle());

    return new DatosGeograficosDTO(
        raw.stream()
            .map(r -> (String) r[0])
            .filter(Objects::nonNull)
            .filter(s -> !s.trim().isEmpty())
            .distinct()
            .collect(Collectors.toList()),
        raw.stream()
            .map(r -> (String) r[1])
            .filter(Objects::nonNull)
            .filter(s -> !s.trim().isEmpty())
            .distinct()
            .collect(Collectors.toList()),
        raw.stream()
            .map(r -> (String) r[2])
            .filter(Objects::nonNull)
            .filter(s -> !s.trim().isEmpty())
            .distinct()
            .collect(Collectors.toList()),
        raw.stream()
            .map(r -> (String) r[3])
            .filter(Objects::nonNull)
            .filter(s -> !s.trim().isEmpty())
            .distinct()
            .collect(Collectors.toList()));
  }

  // ADMIN:Operacion (C)RUD
  @Override
  @Transactional
  public ColeccionDTO crearDesdeDTO(ColeccionDTO coleccionDTO) {
    Coleccion coleccion = new Coleccion(coleccionDTO.getTitulo(), coleccionDTO.getDescripcion());
    establecerDatos(coleccionDTO, coleccion);
    coleccion.setHandle(generarHandleUnico(coleccion.getTitulo()));
    coleccion.actualizarColeccion();
    // Guardar y forzar flush para persistir la relación coleccion-hecho
    coleccionesRepository.saveAndFlush(coleccion);
    return ColeccionDTO.fromColeccion(coleccion);
  }

  // ADMIN:Operacion C(R)UD
  @Override
  public Optional<ColeccionDTO> mostrarColeccion(String handle) {
    Coleccion coleccion = intentarRecuperarColeccion(handle);
    return Optional.of(ColeccionDTO.fromColeccion(coleccion));
  }

  // ADMIN:Operacion CR(U)D
  @Override
  @Transactional
  public ColeccionDTO actualizarColeccion(String handle, ColeccionDTO coleccionDTO) {
    Coleccion coleccion = intentarRecuperarColeccion(handle);
    establecerDatos(coleccionDTO, coleccion);
    coleccion.actualizarColeccion();
    coleccionesRepository.save(coleccion);
    return ColeccionDTO.fromColeccion(coleccion);
  }

  // ADMIN:Operacion CRU(D)
  @Override
  @Transactional
  public void eliminarColeccion(String handle) {
    Coleccion coleccion = intentarRecuperarColeccion(handle);

    // Limpiar todas las asociaciones ManyToMany antes de eliminar
    coleccion.getFuentes().clear();
    coleccion.getCriterios().clear();
    coleccion.getHechos().clear();

    // Guardar para persistir la limpieza de asociaciones
    coleccionesRepository.save(coleccion);

    // Ahora sí eliminar la colección
    coleccionesRepository.deleteColeccionByHandle(coleccion.getHandle());
  }

  private void establecerDatos(ColeccionDTO coleccionDTO, Coleccion coleccion) {
    if (coleccionDTO.getTitulo() != null) {
      coleccion.setTitulo(coleccionDTO.getTitulo());
    }

    if (coleccionDTO.getDescripcion() != null) {
      coleccion.setDescripcion(coleccionDTO.getDescripcion());
    }

    if (coleccionDTO.getAlgoritmo() != null) {
      String nombre = coleccionDTO.getAlgoritmo();
      TipoAlgoritmo tipo = TipoAlgoritmo.valueOf(nombre.toUpperCase());
      EstrategiaConsenso algoritmo = tipo.getConsenso();
      coleccion.setAlgoritmoDeConsenso(algoritmo);
    }

    if (coleccionDTO.getCriterios() != null) {
      List<Filtro> criterios = coleccionDTO.getCriterios().getList();
      criterios.forEach(c -> c.setColeccion(coleccion));
      coleccion.getCriterios().clear();
      coleccion.getCriterios().addAll(criterios);
    }

    if (coleccionDTO.getFuentes() != null) {
      List<Long> fuenteIds = coleccionDTO.getFuentes().stream()
          .map(FuenteOutputDTO::getId)
          .toList();
      // Cargar fuentes con sus hechos para evitar lazy loading en actualizarColeccion()
      List<Fuente> fuentes = fuentesRepository.findAllByIdInWithHechos(fuenteIds);
      coleccion.getFuentes().clear();
      coleccion.getFuentes().addAll(fuentes);
    }
  }

  private Coleccion intentarRecuperarColeccion(String handle) {
    Coleccion coleccion = coleccionesRepository.findColeccionByHandle(handle.trim());
    if (coleccion == null) {
      throw new NotFoundException("Coleccion", handle);
    }
    return coleccion;
  }

  private String generarHandleUnico(String baseTitulo) {
    String base = baseTitulo.toLowerCase().replaceAll("[^a-z0-9]", "");
    String candidato = base;
    for (int i = 1; this.coleccionesRepository.existsByHandle(candidato); i++) {
      candidato = base + i;
    }
    return candidato;
  }

  @Override
  @Transactional
  public void agregarFuente(Long idColeccion, Long idFuente) {

  }

  @Override
  @Transactional
  public void quitarFuente(Long idColeccion, Long idFuente) {

  }

}
