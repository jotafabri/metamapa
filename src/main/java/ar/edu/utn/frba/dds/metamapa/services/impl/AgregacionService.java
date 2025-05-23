package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.*;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgregacionService implements IAgregacionService {

  @Autowired
  private IColeccionesRepository coleccionRepository;
  @Autowired
  private ISolicitudesEliminacionRepository solicitudesRepository;
  @Autowired
  private IHechosRepository hechosRepository;

  @Override
  public void crearSolicitud(SolicitudEliminacionDTO solicitudDto) {
    var hecho = this.hechosRepository.findById(solicitudDto.getIdHecho());
    if (hecho != null) {
      var solicitud = new SolicitudEliminacion(
          hecho,
          solicitudDto.getRazon());
      this.solicitudesRepository.save(solicitud);
    }
  }

  //private final HechoRepository hechoRepo;
  // Esto lo tiene que hacer juan ignacio
  // @Scheduled(fixedRate = 3600000)
  @Override
  public void refrescarColecciones(){
      for(Coleccion coleccion : this.obtenerColecciones()){
          refrescarHechosColeccion(coleccion);
      }
  }

  //TODO
  @Override
  public List<Coleccion> obtenerColecciones() {
    return coleccionRepository.findAll();
  }

  public void refrescarHechosColeccion(Coleccion coleccion) {
    for (Fuente fuente : coleccion.getFuentes()) {

      List<Hecho> hechosActualizados = fuente.getListaHechos();

      List<Hecho> hechosFiltrados = new ArrayList<>();
      for (Hecho hecho : hechosActualizados) {
        boolean cumpleTodos = coleccion.getCriterios().stream()
                .allMatch(criterio -> criterio.cumple(hecho));
        if (cumpleTodos || coleccion.getCriterios().isEmpty()) {
          hechosFiltrados.add(hecho);
        }
      }

      for (Hecho hecho : hechosFiltrados) {
        /*if (fuente instanceof FuenteDinamica fuenteDinamica) {
          boolean fueEliminado = fuenteDinamica.getSolicitudesEliminacion().stream()
                  .anyMatch(s -> s.getEstado().equals(Estado.ACEPTADA)
                          && s.getHecho().getTitulo().equalsIgnoreCase(hecho.getTitulo()));
          if (fueEliminado) continue;

          fuenteDinamica.agregarHecho(hecho);
        }*/

        if (fuente instanceof FuenteEstatica fuenteEstatica) {
          boolean yaExiste = fuenteEstatica.getListaHechos().stream()
                  .anyMatch(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()));
          if (!yaExiste) {
            fuenteEstatica.getListaHechos().add(hecho);
          } else {
            fuenteEstatica.getListaHechos().removeIf(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()));
            fuenteEstatica.getListaHechos().add(hecho);
          }
        }
      }
    }

    System.out.println("Refrescada la colección: " + coleccion.getTitulo());
  }


  @Override
  public void aprobarSolicitud(SolicitudEliminacion solicitud) {

    if (solicitud.getEstado() != Estado.PENDIENTE) {
      throw new IllegalStateException("Solo se pueden aprobar solicitudes pendientes. Estado actual: " + solicitud.getEstado());
    }
    if (solicitud.getHecho() == null) {
      throw new IllegalStateException("La solicitud no tiene un hecho asociado válido");
    }
    solicitud.aceptarSolicitud();
    solicitudesRepository.save(solicitud);

  }

  @Override
  public void rechazarSolicitud(SolicitudEliminacion solicitud) {

    if (solicitud.getEstado() != Estado.PENDIENTE) {
      throw new IllegalStateException("Solo se pueden rechazar solicitudes pendientes");
    }

    if (solicitud.getHecho() == null) {
      throw new IllegalStateException("La solicitud no tiene un hecho asociado válido");
    }

    solicitud.rechazarSolicitud();
    solicitudesRepository.save(solicitud);
  }

  public void findAllSolicitudes(){
    this.solicitudesRepository.findAll();
  }


}

