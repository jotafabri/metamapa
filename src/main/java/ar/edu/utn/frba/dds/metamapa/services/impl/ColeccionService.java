package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.ConsensoAbsoluto;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.ConsensoPorMayoriaSimple;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.ConsensoPorMultiplesMenciones;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.ListaDeFiltros;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.TipoAlgoritmo;
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
                                          String categoria,
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

    return coleccionesRepository.findByHandle(handle)
        .navegar(filtro)
        .stream()
            .filter(h -> h.getEstado() == Estado.ACEPTADA)
            .map(HechoDTO::fromHecho)
        .toList();
  }

  //ADMIN: Muestra todos los hechos de la coleccion , los que no fueron ASEPTADOS tambien.
  @Override
  public List<HechoDTO> getHechosByHandleAdmin(String handle,
                                               String categoria,
                                               String fecha_reporte_desde,
                                               String fecha_reporte_hasta,
                                               String fecha_acontecimiento_desde,
                                               String fecha_acontecimiento_hasta,
                                               String ubicacion,
                                               Boolean soloConMultimedia,
                                               Boolean soloConContribuyente) {
    var filtro = new ListaDeFiltros().getListFromParams(
            categoria,
            fecha_reporte_desde,
            fecha_reporte_hasta,
            fecha_acontecimiento_desde,
            fecha_acontecimiento_hasta,
            ubicacion,
            soloConMultimedia,
            soloConContribuyente
    );

    return coleccionesRepository.findByHandle(handle)
            .navegar(filtro)
            .stream()
            .map(HechoDTO::fromHecho)
            .toList();
  }






  //USUARIO:Navegacion curada sobre una coleccion.
  @Override
  public List<HechoDTO> getHechosCurados(String handle, Boolean curado) {
    return this.coleccionesRepository.findByHandle(handle)
        .navegarCurado(curado)
        .stream()
        .map(HechoDTO::fromHecho)
        .toList();
  }

  //ADMIN:Operacion (C)RUD
  @Override
  public void crearDesdeDTO(ColeccionDTO coleccionDTO) {
    this.coleccionesRepository.save(new Coleccion(coleccionDTO.getTitulo(), coleccionDTO.getDescripcion()));
  }

  //ADMIN:Operacion C(R)UD
  @Override
  public ColeccionDTO mostrarColeccion(String handle) {
    var coleccion = this.coleccionesRepository.findByHandle(handle);
    return ColeccionDTO.fromColeccion(coleccion);
  }

  //ADMIN:Operacion CR(U)D
  @Override
  public void actualizarColeccion(String handle, ColeccionDTO coleccionDTO) {
    var coleccion = this.coleccionesRepository.findByHandle(handle);
    if (coleccionDTO.getTitulo() != null) {
      coleccion.setTitulo(coleccionDTO.getTitulo());
    }
    if (coleccionDTO.getDescripcion() != null) {
      coleccion.setDescripcion(coleccionDTO.getDescripcion());
    }
    this.coleccionesRepository.save(coleccion);
  }
  //ADMIN:Operacion CRU(D)
  @Override
  public void eliminarColeccion(String handle) {

    this.coleccionesRepository.delete(handle);
  }

  //ADMIN:Modificacion de algoritmo de consenso.
  @Override
  public void cambiarAlgoritmo(String handle, TipoAlgoritmo tipo) {
    var coleccion = this.coleccionesRepository.findByHandle(handle);
    if (tipo == TipoAlgoritmo.MAYORIA_ABSOLUTA) {
      coleccion.setAlgoritmoDeConsenso(new ConsensoAbsoluto());
    } else if (tipo == TipoAlgoritmo.MAYORIA_SIMPLE) {
      coleccion.setAlgoritmoDeConsenso(new ConsensoPorMayoriaSimple());
    } else if (tipo == TipoAlgoritmo.MULTIPLES_MENCIONES) {
      coleccion.setAlgoritmoDeConsenso(new ConsensoPorMultiplesMenciones());
    }
    this.coleccionesRepository.save(coleccion);
  }
}
