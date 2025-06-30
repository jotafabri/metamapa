package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.Multimedia;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Origen;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IFuentesRepository;
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

  @Autowired
  private IFuentesRepository fuentesRepository;

  @Autowired
  private HechosService hechosService;

  @Value("${app.base-url}")
  private String baseUrl;




  @Override
  public void init() {
    FuenteDinamica fuenteDinamica = new FuenteDinamica();
      fuentesRepository.save(fuenteDinamica);


    Hecho hecho1 = hechosService.crearHecho(
            "Tsunami Chile",
            "Terremoto causa fuerte tsunami en las costas de Chile",
            "Desastre Natural",
            -36.868375,
            -60.343297,
            LocalDate.parse("27/02/2010", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay()
    );
    hecho1.setMultimedia(new Multimedia(baseUrl, "/images/hechos/es-posible-un-tsunami-en-buenos-aires.jpg"));
    hecho1.aceptar();

    Hecho hecho2 = hechosService.crearHecho(
            "Incendio del Bolson",
            "Incendio causa la perdida de miles de hectarias de bosque",
            "Desastre Natural",
            -11.837396,
            17.541648,
            LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay()
    );
    hecho2.setMultimedia(new Multimedia(baseUrl, "/images/hechos/_126544339_fuego12.jpg"));
    hecho2.aceptar();

    Hecho hecho3 = hechosService.crearHecho(
            "Incendio del Bolson otra Vez",
            "Nuevo incendio causa la perdida de miles de hectarias de bosque",
            "Desastre Natural",
            -11.837396,
            17.541648,
            LocalDate.parse("30/06/2025", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay()
    );
    //hecho3 queda PENDIENTE

    Hecho hecho4 = hechosService.crearHecho(
            "Obvnis en el Uritorco",
            "Avistamiento de obvnis reales en el Uritorco",
            "Paranormal",
            -12.835496,
            17.541654,
            LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay()
    );
    hecho4.setMultimedia(new Multimedia(baseUrl, "/images/hechos/artworks-000351288021-i1m1am-t500x500.jpg"));
    hecho4.rechazar();

    var hechos = List.of(hecho1, hecho2,hecho3,hecho4);

    hechos.forEach(hechosRepository::save);
      List<Hecho> hechosAgregar = new ArrayList<>(hechos);

    for (Hecho hecho : hechosAgregar) {
      fuenteDinamica.agregarHecho(hecho);
    }


    Coleccion coleccionPrueba = new Coleccion("Colección prueba", "Esto es una prueba");
    coleccionPrueba.agregarFuente(fuenteDinamica);

    this.coleccionesRepository.save(coleccionPrueba);
    // Cargo fuentes estáticas
    var rutas = List.of(
        baseUrl + "/csv/desastres_naturales_argentina.csv",
        baseUrl + "/csv/desastres_sanitarios_contaminacion_argentina.csv",
        baseUrl + "/csv/desastres_tecnologicos_argentina.csv"
    );

    for (String ruta : rutas) {
      var fuente = new FuenteEstatica();
      fuente.importarHechos(ruta);
      fuentesRepository.save(fuente);
      fuente.getHechos().forEach(h -> this.hechosRepository.save(h));
    }
  }
}
