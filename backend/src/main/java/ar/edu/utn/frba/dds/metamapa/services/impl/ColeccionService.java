package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.util.List;
import java.util.Optional;

import ar.edu.utn.frba.dds.metamapa.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.metamapa.models.dtos.input.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.EstrategiaConsenso;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.TipoAlgoritmo;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.services.IColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColeccionService implements IColeccionService {

  @Autowired
  private IColeccionesRepository coleccionesRepository;

  //ADMIN:Operacion C(R)UD
  @Override
  public List<ColeccionDTO> getAllColecciones() {
    return this.coleccionesRepository.findAll()
        .stream()
        .map(ColeccionDTO::fromColeccion)
        .toList();
  }

  //USUARIO: Navegacion filtrada sobre una coleccion , no muestra los RECHAZADOS y PENDIENTES.
  @Override
  public List<HechoDTO> getHechosByHandle(String handle,
                                          HechoFiltroDTO filtros
  ) {
    Coleccion coleccion = intentarRecuperarColeccion(handle);
    return coleccionesRepository.findColeccionByHandle(coleccion.getHandle())
        .navegar(filtros.getList(), filtros.getCurado())
        .stream()
        .filter(h -> h.getEstado().equals(Estado.ACEPTADA))
        .map(HechoDTO::fromHecho)
        .toList();
  }

  //ADMIN: Muestra todos los hechos de la coleccion , los que no fueron ACEPTADOS tambien.
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

  public List<String> getCategoriasByHandle(String handle) {
    Coleccion coleccion = intentarRecuperarColeccion(handle);
    return coleccionesRepository.findDistinctCategoriasByHandle(coleccion.getHandle());
  }

  //ADMIN:Operacion (C)RUD
  @Override
  public ColeccionDTO crearDesdeDTO(ColeccionDTO coleccionDTO) {
    Coleccion coleccion = new Coleccion(coleccionDTO.getTitulo(), coleccionDTO.getDescripcion());
    coleccion.setHandle(generarHandleUnico(coleccion.getTitulo()));
    this.coleccionesRepository.save(coleccion);
    return ColeccionDTO.fromColeccion(coleccion);
  }

  //ADMIN:Operacion C(R)UD
  @Override
  public Optional<ColeccionDTO> mostrarColeccion(String handle) {
    Coleccion coleccion = intentarRecuperarColeccion(handle);
    return Optional.of(ColeccionDTO.fromColeccion(coleccion));
  }

  //ADMIN:Operacion CR(U)D
  @Override
  public ColeccionDTO actualizarColeccion(String handle, ColeccionDTO coleccionDTO) {
    Coleccion coleccion = intentarRecuperarColeccion(handle);

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

    coleccionesRepository.save(coleccion);

    return ColeccionDTO.fromColeccion(coleccion);
  }

  //ADMIN:Operacion CRU(D)
  @Override
  public void eliminarColeccion(String handle) {
    Coleccion coleccion = intentarRecuperarColeccion(handle);
    coleccionesRepository.deleteColeccionByHandle(coleccion.getHandle());
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
}
