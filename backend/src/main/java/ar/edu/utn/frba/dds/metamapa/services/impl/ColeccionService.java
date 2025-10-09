package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
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
    return this.coleccionesRepository.findColeccionByHandle(handle)
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
    return coleccionesRepository.findColeccionByHandle(handle)
        .navegar(filtros.getList(), false)
        .stream()
        .map(HechoDTO::fromHecho)
        .toList();
  }

  //ADMIN:Operacion (C)RUD
  @Override
  public void crearDesdeDTO(ColeccionDTO coleccionDTO) {
    Coleccion coleccion = new Coleccion(coleccionDTO.getTitulo(), coleccionDTO.getDescripcion());
    coleccion.setHandle(generarHandleUnico(coleccion.getTitulo()));
    this.coleccionesRepository.save(coleccion);
  }

  //ADMIN:Operacion C(R)UD
  @Override
  public ColeccionDTO mostrarColeccion(String handle) {
    var coleccion = this.coleccionesRepository.findColeccionByHandle(handle);
    return ColeccionDTO.fromColeccion(coleccion);
  }

  //ADMIN:Operacion CR(U)D
  @Override
  public void actualizarColeccion(String handle, ColeccionDTO coleccionDTO) {
    var coleccion = this.coleccionesRepository.findColeccionByHandle(handle);
    if (coleccionDTO.getTitulo() != null) {
      coleccion.setTitulo(coleccionDTO.getTitulo());
    }
    if (coleccionDTO.getDescripcion() != null) {
      coleccion.setDescripcion(coleccionDTO.getDescripcion());
    }
    if (coleccionDTO.getAlgoritmo() != null) {
      var nombre = coleccionDTO.getAlgoritmo();
      TipoAlgoritmo tipo = TipoAlgoritmo.valueOf(nombre.toUpperCase());
      var algoritmo = tipo.getConsenso();
      coleccion.setAlgoritmoDeConsenso(algoritmo);
    }
    this.coleccionesRepository.save(coleccion);
  }

  //ADMIN:Operacion CRU(D)
  @Override
  public void eliminarColeccion(String handle) {
    this.coleccionesRepository.deleteColeccionByHandle(handle);
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
