package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.services.IAlgoritmoConsensoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlgoritmoConsensoService implements IAlgoritmoConsensoService {

  @Autowired
  private IColeccionesRepository coleccionesRepository;

  @Transactional
  public void ejecutarAlgoritmos() {
    for (Coleccion coleccion : this.coleccionesRepository.findAll()) {
      coleccion.actualizarCurados();
    }
  }
}
