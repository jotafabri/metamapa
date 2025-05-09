package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.FuenteEstatica;
import ar.edu.utn.frba.dds.metamapa.models.entities.LectorCSV;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.services.ISeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeederService implements ISeederService {
  @Autowired
  private IHechosRepository hechosRepository;

  @Autowired
  private IColeccionesRepository coleccionesRepository;

  @Override
  public void init() {
    // Cargo fuentes estáticas
    var lector = new LectorCSV();
    var rutas = List.of(
        "src/test/desastres_naturales_argentina.csv",
        "src/test/desastres_sanitarios_contaminacion_argentina.csv",
        "src/test/desastres_tecnologicos_argentina.csv"
    );

    for (String ruta : rutas) {
      var fuente = new FuenteEstatica();
      fuente.importarHechos(lector, ruta);
      fuente.getListaHechos().forEach(h -> hechosRepository.save(h));
    }
  }
}
