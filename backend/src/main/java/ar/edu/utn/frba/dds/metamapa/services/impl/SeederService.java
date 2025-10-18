package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Usuario;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Origen;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Permiso;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Rol;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IFuentesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IUsuarioRepository;
import ar.edu.utn.frba.dds.metamapa.services.ISeederService;
import ar.edu.utn.frba.dds.metamapa.services.normalizador.MapeadorTexto;
import ar.edu.utn.frba.dds.metamapa.services.normalizador.NormalizadorFuerte;
import ar.edu.utn.frba.dds.metamapa.services.normalizador.NormalizadorLigero;
import ar.edu.utn.frba.dds.metamapa.services.normalizador.ValidadorFechas;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Autowired
  private IUsuarioRepository usuarioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;


  @Value("${app.base-url}")
  private String baseUrl;

  @PostConstruct
  @Override
  @Transactional // ← Agregar transaccional para manejar todo en una transacción
  public void init() {
    // Limpiar base de datos
    coleccionesRepository.deleteAll();
    fuentesRepository.deleteAll();
    hechosRepository.deleteAll();
    usuarioRepository.deleteAll();


    // === CREAR USUARIOS DE PRUEBA ===

    Usuario admin = new Usuario();
    admin.setNombre("Tomas");
    admin.setEmail("admin@hotmail.com");
    admin.setPassword(passwordEncoder.encode("1234"));
    admin.setRol(Rol.ADMIN);
    admin.setPermisos(List.of(Permiso.EDITAR_HECHOS, Permiso.CREAR_HECHOS, Permiso.ELIMINAR_HECHOS, Permiso.ADMINISTRAR_COLECCIONES));

    Usuario contribuyente = new Usuario();
    contribuyente.setNombre("Marcos");
    contribuyente.setEmail("contribuyente@hotmail.com");
    contribuyente.setPassword(passwordEncoder.encode("1234"));
    contribuyente.setRol(Rol.USER);
    contribuyente.setPermisos(List.of(Permiso.CREAR_HECHOS));

    usuarioRepository.save(admin);
    usuarioRepository.save(contribuyente);

    System.out.println("=== USUARIOS DE PRUEBA CREADOS ===");
    usuarioRepository.findAll().forEach(u ->
            System.out.println("- " + u.getEmail() + " [" + u.getRol() + "]")
    );







    //Inicializo Mappeadores
    MapeadorTexto mapeadorUbicaciones;
    MapeadorTexto mapeadorCategorias;
    try {
      mapeadorUbicaciones = new MapeadorTexto("static/json/diccionario_ubicaciones.json");
      mapeadorCategorias = new MapeadorTexto("static/json/diccionario_categorias.json");
    } catch (IOException e) {
      throw new RuntimeException("No se pudo cargar el JSON de normalización", e);
    }

    // Instanciar normalizadores
    NormalizadorLigero normalizadorLigero = new NormalizadorLigero();
    NormalizadorFuerte normalizadorFuerte = new NormalizadorFuerte(
        normalizadorLigero,
        mapeadorCategorias,
        mapeadorUbicaciones,
        new ValidadorFechas()
    );
    // 1. CREAR Y GUARDAR FUENTES DINÁMICAS PRIMERO
    FuenteDinamica fuenteDinamica = new FuenteDinamica();
    FuenteDinamica fuenteDinamica2 = new FuenteDinamica();
    FuenteDinamica fuenteDinamica3 = new FuenteDinamica();
    FuenteDinamica fuenteDinamica4 = new FuenteDinamica();
    FuenteDinamica fuenteDinamica5 = new FuenteDinamica();

    fuentesRepository.saveAll(List.of(fuenteDinamica, fuenteDinamica2, fuenteDinamica3, fuenteDinamica4, fuenteDinamica5));

    // 2. CREAR HECHOS
    Hecho hecho1 = hechosService.crearHecho(
        "Tsunami Chile",
        "Terremoto causa fuerte tsunami en las costas de Chile",
        "Desastre Natural",
        -33.4489, -70.6693, // Santiago, Chile
        LocalDate.parse("27/02/2010", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atTime(14, 30)
    );
    hecho1.agregarMultimedia("/images/hechos/es-posible-un-tsunami-en-buenos-aires.jpg");

    Hecho hecho2 = hechosService.crearHecho(
        "Incendio California",
        "Incendio forestal causa evacuaciones masivas",
        "Desastre Natural",
        34.0522, -118.2437, // Los Angeles, California, USA
        LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atTime(14, 45)
    );
    hecho2.agregarMultimedia("/images/hechos/_126544339_fuego12.jpg");

    Hecho hecho3 = hechosService.crearHecho(
        "Terremoto Japón",
        "Fuerte terremoto sacude la región de Tokio",
        "Desastre Natural",
        35.6762, 139.6503, // Tokio, Japón
        LocalDate.parse("30/06/2025", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atTime(6, 15)
    );

    Hecho hecho4 = hechosService.crearHecho(
        "Círculos de cosecha Inglaterra",
        "Aparición misteriosa de círculos en cultivos",
        "Paranormal",
        51.1789, -1.8262, // Wiltshire, Inglaterra
        LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atTime(22, 15)
    );
    hecho4.agregarMultimedia("/images/hechos/artworks-000351288021-i1m1am-t500x500.jpg");
    hecho4.rechazar();

    Hecho hecho5 = hechosService.crearHecho(
        "Aurora Boreal Noruega",
        "Increíble aurora boreal vista desde Tromsø",
        "Otros",
        69.6496, 18.9560, // Tromsø, Noruega
        LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atTime(9, 00)
    );
    hecho5.aceptar();

    List<Hecho> hechosDinamicos = List.of(hecho1, hecho2, hecho3, hecho4, hecho5);
    // NORMALIZAR HECHOS DINÁMICOS (ligero)
    hechosDinamicos.forEach(h -> {
      normalizadorLigero.normalizar(h);
      h.aceptar();
      h.setOrigen(Origen.CONTRIBUYENTE);
    });

    // 5. GUARDAR FUENTES DINÁMICAS ACTUALIZADAS
//    fuentesRepository.saveAll(List.of(fuenteDinamica, fuenteDinamica2, fuenteDinamica3, fuenteDinamica4));

    // 4. AHORA AGREGAR HECHOS A LAS FUENTES DINAMICAS
    hecho1.setFuente(fuenteDinamica);
    hecho3.setFuente(fuenteDinamica);
    fuenteDinamica.agregarHecho(hecho1);
    fuenteDinamica.agregarHecho(hecho3);

    hecho2.setFuente(fuenteDinamica2);
    fuenteDinamica2.agregarHecho(hecho2);

    hecho4.setFuente(fuenteDinamica3);
    fuenteDinamica3.agregarHecho(hecho4);

    hecho5.setFuente(fuenteDinamica4);
    fuenteDinamica4.agregarHecho(hecho5);


    // 3. GUARDAR TODOS LOS HECHOS DINAMICOS
    hechosRepository.saveAll(hechosDinamicos);

    /*
    // 6. CREAR Y GUARDAR FUENTES ESTÁTICAS
    var rutas = List.of(
        "static/csv/desastres_naturales_argentina.csv",
        "static/csv/desastres_sanitarios_contaminacion_argentina.csv",
        "static/csv/desastres_tecnologicos_argentina.csv"
    );

    List<FuenteEstatica> fuentesEstaticasCreadas = new ArrayList<>();

    for (String ruta : rutas) {
      var fuente = new FuenteEstatica(ruta);

      // Guardar la fuente
      fuente = this.fuentesRepository.save(fuente);
      fuentesEstaticasCreadas.add(fuente);

      // Normalizar cada hecho usando NormalizadorFuerte
      for (Hecho hecho : fuente.getHechos()) {
        hecho = normalizadorFuerte.normalizar(hecho);
        hecho.setFuente(fuente);
      }

      // Guardar hechos normalizados
      this.hechosRepository.saveAll(fuente.getHechos());


    }
*/

    // 7. CREAR COLECCIONES
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
    coleccionPrueba2.agregarFuente(fuenteDinamica4); // con fuente del hecho aislado
    coleccionPrueba2.actualizarColeccion();
    coleccionPrueba2.actualizarCurados();
    this.coleccionesRepository.save(coleccionPrueba2);

    Coleccion coleccionPrueba3 = new Coleccion("Coleccion prueba3", "Esto es una prueba3");
    coleccionPrueba3.agregarFuente(fuenteDinamica);
    coleccionPrueba3.agregarFuente(fuenteDinamica2);
    coleccionPrueba3.agregarFuente(fuenteDinamica3);
    coleccionPrueba3.agregarFuente(fuenteDinamica4);


    /*
    // Agregar fuentes estáticas (ya guardadas)
    for (FuenteEstatica fuente : fuentesEstaticasCreadas) {
      coleccionPrueba3.agregarFuente(fuente);
    }

    coleccionPrueba3.actualizarColeccion();
    coleccionPrueba3.actualizarCurados();
    coleccionesRepository.save(coleccionPrueba3);
*/

  }

}