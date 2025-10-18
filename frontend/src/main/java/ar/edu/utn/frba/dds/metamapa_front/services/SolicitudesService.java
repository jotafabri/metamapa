package ar.edu.utn.frba.dds.metamapa_front.services;

import java.util.Optional;

import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolicitudesService {
  @Autowired
  private MetamapaApiService metamapaApiService;

  public void crearSolicitud(SolicitudEliminacionDTO solicitudDTO) {
    metamapaApiService.crearSolicitudEliminacion(solicitudDTO);
  }

  public void aceptarSolicitud(Long id) {
    metamapaApiService.aceptarSolicitudEliminacion(id);
  }

  public void rechazarSolicitud(Long id) {
    metamapaApiService.rechazarSolicitudEliminacion(id);
  }
}
