package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgregacionService implements IAgregacionService {

  private IColeccionesRepository coleccionRepository;
  //private final HechoRepository hechoRepo;

  @Scheduled(fixedRate = 3600000)
  @Override
  public void refrescarColecciones() {
    for (Coleccion coleccion : this.obtenerColecciones()) {
      refrescarHechosColeccion(coleccion);
    }
  }

  //TODO
  public List<Coleccion> obtenerColecciones() {
    return coleccionRepository.findAll();
  }

  private void refrescarHechosColeccion(Coleccion coleccion) {


  }


}

