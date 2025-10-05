package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.Filtro;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl.*;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IFuentesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.services.ISeederService;
import ar.edu.utn.frba.dds.metamapa.services.normalizador.MapeadorTexto;
import ar.edu.utn.frba.dds.metamapa.services.normalizador.NormalizadorFuerte;
import ar.edu.utn.frba.dds.metamapa.services.normalizador.NormalizadorLigero;
import ar.edu.utn.frba.dds.metamapa.services.normalizador.ValidadorFechas;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.base-url}")
    private String baseUrl;

    @PostConstruct
    @Override
    @Transactional
    public void init() {

        // Limpiar base de datos
        coleccionesRepository.deleteAll();
        fuentesRepository.deleteAll();
        hechosRepository.deleteAll();

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
        hecho1.aceptar();

        Hecho hecho2 = hechosService.crearHecho(
                "Incendio California",
                "Incendio forestal causa evacuaciones masivas",
                "Desastre Natural",
                34.0522, -118.2437, // Los Angeles, California, USA
                LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atTime(14, 45)
        );
        hecho2.agregarMultimedia("/images/hechos/_126544339_fuego12.jpg");
        hecho2.aceptar();

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
        hechosDinamicos.forEach(normalizadorLigero::normalizar);

        // 3. GUARDAR TODOS LOS HECHOS DINAMICOS
        hechosRepository.saveAll(hechosDinamicos);

        // 4. AHORA AGREGAR HECHOS A LAS FUENTES DINAMICAS
        fuenteDinamica.agregarHecho(hecho1);
        fuenteDinamica.agregarHecho(hecho2);
        fuenteDinamica.agregarHecho(hecho3);
        fuenteDinamica.agregarHecho(hecho4);

        fuenteDinamica2.agregarHecho(hecho1);
        fuenteDinamica2.agregarHecho(hecho2);

        fuenteDinamica3.agregarHecho(hecho1);

        fuenteDinamica4.agregarHecho(hecho5);

        // 5. GUARDAR FUENTES DINÁMICAS ACTUALIZADAS
        fuentesRepository.saveAll(List.of(fuenteDinamica, fuenteDinamica2, fuenteDinamica3, fuenteDinamica4));
        // 6. CREAR Y GUARDAR FUENTES ESTÁTICAS
        var rutas = List.of(
                "static/csv/desastres_naturales_argentina.csv",
                "static/csv/desastres_sanitarios_contaminacion_argentina.csv",
                "static/csv/desastres_tecnologicos_argentina.csv"
        );

        List<FuenteEstatica> fuentesEstaticasCreadas = new ArrayList<>();

        for (String ruta : rutas) {
            var fuente = new FuenteEstatica(ruta);

            // Normalizar cada hecho usando NormalizadorFuerte
            for (Hecho hecho : fuente.getHechos()) {
                hecho = normalizadorFuerte.normalizar(hecho);
            }

            // Guardar hechos normalizados
            this.hechosRepository.saveAll(fuente.getHechos());

            // Guardar la fuente
            fuente = this.fuentesRepository.save(fuente);
            fuentesEstaticasCreadas.add(fuente);
        }




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

        // Agregar fuentes estáticas (ya guardadas)
        for (FuenteEstatica fuente : fuentesEstaticasCreadas) {
            coleccionPrueba3.agregarFuente(fuente);
        }

        coleccionPrueba3.actualizarColeccion();
        coleccionPrueba3.actualizarCurados();
        coleccionesRepository.save(coleccionPrueba3);

           /* --- BLOQUE DE PRUEBA DE FILTROS --- */
                Filtro categoriaFiltro = new FiltroCategoria("Desastre Natural");
        Filtro fechaFiltro = new FiltroFechaAcontecimiento(
                LocalDate.parse("01/01/2000", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(),
                LocalDate.parse("31/12/2025", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atTime(23,59)
        );
        Filtro multimediaFiltro = new FiltroMultimedia(true);
        Filtro contribuyenteFiltro = new FiltroContribuyente(true);
        Filtro ubicacionFiltro = FiltroUbicacion.fromString("-33.4489, -70.6693"); // Santiago

        System.out.println("=== RESULTADOS DE FILTROS ===");
        for (Hecho hecho : hechosDinamicos) {
            System.out.println("Hecho: " + hecho.getTitulo());
            System.out.println("  Categoria Desastre Natural? " + categoriaFiltro.cumple(hecho));
            System.out.println("  Fecha dentro rango? " + fechaFiltro.cumple(hecho));
            System.out.println("  Tiene multimedia? " + multimediaFiltro.cumple(hecho));
            System.out.println("  Tiene contribuyente? " + contribuyenteFiltro.cumple(hecho));
            System.out.println("  Esta en Santiago? " + ubicacionFiltro.cumple(hecho));
        }


    }


}