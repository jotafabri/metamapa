package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.FuenteDinamica;
import ar.edu.utn.frba.dds.metamapa.models.entities.FuenteEstatica;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.LectorCSV;
import ar.edu.utn.frba.dds.metamapa.models.entities.Origen;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.services.ISeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SeederService implements ISeederService {
  @Autowired
  private IHechosRepository hechosRepository;

  @Autowired
  private IColeccionesRepository coleccionesRepository;

  @Value("${app.base-url}")
  private String baseUrl;

  @Override
  public void init() {
    FuenteDinamica fuenteDinamica = new FuenteDinamica();
    List<Hecho> hechosAgregar = new ArrayList();
    hechosAgregar.add(new Hecho("Caída de aeronave impacta en Olavarría", "Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
        "Caída de aeronave", (float) -36.868375, (float) -60.343297,
        LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(), Origen.CARGA_MANUAL));
    for (Hecho hecho : hechosAgregar) {
      fuenteDinamica.agregarHecho(hecho);
    }
    Coleccion coleccionPrueba = new Coleccion("Colección prueba", "Esto es una prueba", null, null);
    coleccionPrueba.setFuente(fuenteDinamica);

    this.coleccionesRepository.save(coleccionPrueba);
    // Cargo fuentes estáticas
    var lector = new LectorCSV();
    var rutas = List.of(
        baseUrl + "/csv/desastres_naturales_argentina.csv",
        baseUrl + "/csv/desastres_sanitarios_contaminacion_argentina.csv",
        baseUrl + "/csv/desastres_tecnologicos_argentina.csv"
    );

    for (String ruta : rutas) {
      var fuente = new FuenteEstatica();
      fuente.importarHechos(lector, ruta);
      fuente.getListaHechos().forEach(h -> this.hechosRepository.save(h));
    }
  }
}
