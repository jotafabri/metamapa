package ar.edu.utn.frba.dds.metamapa;

import ar.edu.utn.frba.dds.metamapa.models.entities.*;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Origen;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.Filtro;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.metamapa.models.repositories.impl.ColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.services.impl.AgregacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AggregationServiceTest {

    private ColeccionesRepository mockColeccionRepo;
    private AgregacionService agregacionService;

    @BeforeEach
    void setUp() {
        mockColeccionRepo = Mockito.mock(ColeccionesRepository.class);
        agregacionService = new AgregacionService(mockColeccionRepo);
    }

    @Test
    void testRefrescarColecciones() {

        Hecho hecho = new Hecho("Título", "Descripción", "Incendio", (double) -34.6, (double) -58.4, LocalDateTime.now(), Origen.DATASET);

        FuenteEstatica fuenteEstatica = new FuenteEstatica();
        fuenteEstatica.getHechos().add(hecho);

        Filtro criterioTrue = h -> true;

        Coleccion coleccion = new Coleccion("Incendios", "Hechos de incendios", List.of(criterioTrue));

        Mockito.when(mockColeccionRepo.findAll()).thenReturn(List.of(coleccion));

        agregacionService.refrescarColecciones();

        assertEquals(1, fuenteEstatica.getHechos().size());
        assertEquals("Título", fuenteEstatica.getHechos().get(0).getTitulo());
    }
/*
    @Test
    void testNoReagregaHechoEliminadoEnFuenteDinamica() {
        Hecho hechoEliminado = new Hecho("Título", "Desc original", "Incendio", new Coordenadas(-34.7f, -58.5f), LocalDateTime.now(), Origen.DATASET);

        Hecho hechoReaparecido = new Hecho("Título", "Reaparece", "Incendio", new Coordenadas(-34.8f, -58.6f), LocalDateTime.now(), Origen.DATASET);

        FuenteDinamica fuenteDinamica = new FuenteDinamica() {
            private final List<Hecho> hechosAgregados = new java.util.ArrayList<>();
            private final List<Hecho> hechosEntrantes = List.of(hechoReaparecido);
            private final List<SolicitudEliminacion> solicitudes = List.of(new SolicitudEliminacion(hechoEliminado, "Contenido sensible"));

            {
                solicitudes.get(0).aceptarSolicitud();
            }

            @Override
            public List<Hecho> getListaHechos() {
                return hechosEntrantes;
            }

            @Override
            public void agregarHecho(Hecho hecho) {
                hechosAgregados.add(hecho);
            }

            @Override
            public List<Hecho> getHechosAgregados() {
                return hechosAgregados;
            }

            @Override
            public List<SolicitudEliminacion> getSolicitudesEliminacion() {
                return solicitudes;
            }
        };

        Coleccion coleccion = new Coleccion("Colección", "desc", "handle", fuenteDinamica, List.of(h -> true));
        Mockito.when(mockColeccionRepo.findAll()).thenReturn(List.of(coleccion));

        agregacionService.refrescarColecciones();

        long cantidad = fuenteDinamica.getHechosAgregados().stream()
                .filter(h -> h.getTitulo().equalsIgnoreCase("Título"))
                .count();

        assertEquals(0, cantidad, "El hecho eliminado no debe re-agregarse");
    }
*/



    @Test
    void testPisadoHechoExistenteEnFuenteEstatica() {

        Hecho hechoOriginal = new Hecho("Título", "Descripción vieja", "Incendio", (double) -40.1, (double) -60.1, LocalDateTime.now().minusDays(1), Origen.DATASET);
        FuenteEstatica fuenteEstatica = new FuenteEstatica();
        fuenteEstatica.getHechos().add(hechoOriginal);

        Hecho hechoNuevo = new Hecho("Título", "Descripción NUEVA", "Incendio", (double) -40.5, (double) -60.5, LocalDateTime.now(), Origen.DATASET);
        fuenteEstatica.getHechos().add(hechoNuevo); // simulamos que se "actualizó" como en un dataset real

        Coleccion coleccion = new Coleccion("Colección", "desc", List.of(h -> true));
        Mockito.when(mockColeccionRepo.findAll()).thenReturn(List.of(coleccion));

        agregacionService.refrescarColecciones();

        List<Hecho> hechos = fuenteEstatica.getHechos().stream()
                .filter(h -> h.getTitulo().equalsIgnoreCase("Título"))
                .toList();

        assertEquals(1, hechos.size(), "Debe haber un solo hecho con ese título");
        assertEquals("Descripción NUEVA", hechos.get(0).getDescripcion(), "Debe ser la versión actualizada del hecho");
        assertEquals((double) -40.5, hechos.get(0).getLatitud(), 0.01);
    }

}
