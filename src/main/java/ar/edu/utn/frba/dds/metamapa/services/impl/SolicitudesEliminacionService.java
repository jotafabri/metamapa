package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.SolicitudEliminacion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.metamapa.services.ISolicitudesEliminacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolicitudesEliminacionService implements ISolicitudesEliminacionService {

  @Autowired
  private ISolicitudesEliminacionRepository solicitudesRepository;

  @Autowired
  private IHechosRepository hechosRepository;

  @Override
  public void crearSolicitud(SolicitudEliminacionDTO solicitudDTO) {
    var hecho = this.hechosRepository.findById(solicitudDTO.getIdHecho());
    if (hecho != null) {
      var solicitud = new SolicitudEliminacion(
          hecho,
          solicitudDTO.getRazon());
      this.solicitudesRepository.agregarSolicitud(solicitud);
    }
  }


}
