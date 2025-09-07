package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
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
    fuenteDinamica.setId(1L);
    FuenteDinamica fuenteDinamica2 = new FuenteDinamica();
    fuenteDinamica2.setId(2L);
    FuenteDinamica fuenteDinamica3 = new FuenteDinamica();
    fuenteDinamica3.setId(3L);
    FuenteDinamica fuenteDinamica4 = new FuenteDinamica();
    fuenteDinamica4.setId(4L);
    FuenteDinamica fuenteDinamica5 = new FuenteDinamica();
    fuenteDinamica5.setId(5L);
    fuentesRepository.save(fuenteDinamica);
    fuentesRepository.save(fuenteDinamica2);
    fuentesRepository.save(fuenteDinamica3);
    fuentesRepository.save(fuenteDinamica4);
    fuentesRepository.save(fuenteDinamica5);


    Hecho hecho1 = hechosService.crearHecho(
        "Tsunami Chile",
        "Terremoto causa fuerte tsunami en las costas de Chile",
        "Desastre Natural",
        -36.868375,
        -60.343297,
        LocalDate.parse("27/02/2010", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay()
    );
    hecho1.agregarMultimedia("/images/hechos/es-posible-un-tsunami-en-buenos-aires.jpg");
    hecho1.aceptar();
    //hecho1 ACEPTADO
    Hecho hecho2 = hechosService.crearHecho(
        "Incendio del Bolson",
        "Incendio causa la perdida de miles de hectarias de bosque",
        "Desastre Natural",
        -11.837396,
        17.541648,
        LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay()
    );
    hecho2.agregarMultimedia("/images/hechos/_126544339_fuego12.jpg");
    hecho2.aceptar();
    //hecho2 ACEPTADO

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
    hecho4.agregarMultimedia("/images/hechos/artworks-000351288021-i1m1am-t500x500.jpg");
    hecho4.rechazar();
    //hecho4 RECHAZADO

    Hecho hecho5 = hechosService.crearHecho(
        "Este es un hecho aislado",
        "Suceso Aislado",
        "Otros",
        -12.835496,
        17.541654,
        LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay()
    );
    hecho5.aceptar();
    //hecho5 ACEPTADO


    var hechos = List.of(hecho1, hecho2, hecho3, hecho4);

    hechos.forEach(hechosRepository::save);
    List<Hecho> hechosAgregar = new ArrayList<>(hechos);

    //fuenteDinamica tiene todos los hechos dinamicos menos el aislado.
    for (Hecho hecho : hechosAgregar) {
      fuenteDinamica.agregarHecho(hecho);
    }
    //fuenteDinamica2 tiene solo el hecho1 y el hecho2.
    fuenteDinamica2.agregarHecho(hecho1);
    fuenteDinamica2.agregarHecho(hecho2);

    //fuenteDinamica3 tiene solo el hecho1.
    fuenteDinamica3.agregarHecho(hecho1);

    //fuenteDinamica4 tiene el hecho5(hecho aislado pertenesiente solo a esta fuente).
    fuenteDinamica4.agregarHecho(hecho5);

    //fuenteDinamica5 no tiene hechos.


    // Cargo fuentes estáticas
    var rutas = List.of(
        baseUrl + "/csv/desastres_naturales_argentina.csv",
        baseUrl + "/csv/desastres_sanitarios_contaminacion_argentina.csv",
        baseUrl + "/csv/desastres_tecnologicos_argentina.csv"
    );

    List<FuenteEstatica> fuentesEstaticasCreadas = new ArrayList<>();

    for (String ruta : rutas) {
      var fuente = new FuenteEstatica(ruta);
      fuentesRepository.save(fuente);
      fuente.getHechos().forEach(h -> this.hechosRepository.save(h));
      fuentesEstaticasCreadas.add(fuente);
    }


    //Colecciones
    Coleccion coleccionPrueba = new Coleccion("Coleccion prueba", "Esto es una prueba");
    coleccionPrueba.agregarFuente(fuenteDinamica);
    coleccionPrueba.agregarFuente(fuenteDinamica2);
    coleccionPrueba.agregarFuente(fuenteDinamica3);
    coleccionPrueba.actualizarColeccion();
    coleccionPrueba.actualizarCurados();
    this.coleccionesRepository.save(coleccionPrueba);


    Coleccion coleccionPrueba2 = new Coleccion("Coleccion prueba2", "Esto es una prueba2");
    coleccionPrueba2.agregarFuente(fuenteDinamica);
    coleccionPrueba2.agregarFuente(fuenteDinamica2);
    coleccionPrueba2.agregarFuente(fuenteDinamica3);
    coleccionPrueba2.agregarFuente(fuenteDinamica4); //con fuente del hecho aislado
    coleccionPrueba2.actualizarColeccion();
    coleccionPrueba2.actualizarCurados();
    this.coleccionesRepository.save(coleccionPrueba2);


    Coleccion coleccionPrueba3 = new Coleccion("Coleccion prueba3", "Esto es una prueba3");
    coleccionPrueba3.agregarFuente(fuenteDinamica);
    coleccionPrueba3.agregarFuente(fuenteDinamica2);
    coleccionPrueba3.agregarFuente(fuenteDinamica3);
    coleccionPrueba3.agregarFuente(fuenteDinamica4);

    //Agrego fuentes estaticas
    for (FuenteEstatica fuente : fuentesEstaticasCreadas) {
      coleccionPrueba3.agregarFuente(fuente);
    }

    coleccionPrueba3.actualizarColeccion();
    coleccionPrueba3.actualizarCurados();
    coleccionesRepository.save(coleccionPrueba3);


  }
}
