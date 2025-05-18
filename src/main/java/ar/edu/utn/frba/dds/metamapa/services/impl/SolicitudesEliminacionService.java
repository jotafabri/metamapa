package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.SolicitudEliminacionDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.SolicitudEliminacion;
import ar.edu.utn.frba.dds.metamapa.services.ISolicitudesEliminacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolicitudesEliminacionService implements ISolicitudesEliminacionService {

  @Autowired
  private ISolicitudesEliminacionService solicitudesRepository;

  //TODO : aqui habia un @Override (nose porque)
  private SolicitudEliminacionDTO crear_solicitudDTO(SolicitudEliminacion solicitud){
    SolicitudEliminacionDTO solicitudDTO = new SolicitudEliminacionDTO(solicitud.getHecho(), solicitud.getCausa());
    return solicitudDTO;
  }



}
