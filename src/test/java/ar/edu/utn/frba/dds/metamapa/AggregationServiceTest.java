package ar.edu.utn.frba.dds.metamapa;

import ar.edu.utn.frba.dds.metamapa.models.entities.*;
import ar.edu.utn.frba.dds.metamapa.models.repositories.impl.ColeccionRepository;
import ar.edu.utn.frba.dds.metamapa.services.impl.AgregacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AggregationServiceTest {

    private ColeccionRepository mockColeccionRepo;
    private AgregacionService agregacionService;

    @BeforeEach
    void setUp() {
        mockColeccionRepo = Mockito.mock(ColeccionRepository.class);
        agregacionService = new AgregacionService(mockColeccionRepo);
    }

    @Test
    void testRefrescarColecciones() {

        Hecho hecho = new Hecho("Título", "Descripción", "Incendio", new Coordenadas((float) -34.6, (float) -58.4), LocalDateTime.now(), Origen.DATASET);

        FuenteEstatica fuenteEstatica = new FuenteEstatica();
        fuenteEstatica.getListaHechos().add(hecho);

        CriterioPertenencia criterioTrue = h -> true;

        Coleccion coleccion = new Coleccion("Incendios", "Hechos de incendios", "handle", fuenteEstatica, List.of(criterioTrue));

        Mockito.when(mockColeccionRepo.findAll()).thenReturn(List.of(coleccion));

        agregacionService.refrescarColecciones();

        assertEquals(1, fuenteEstatica.getListaHechos().size());
        assertEquals("Título", fuenteEstatica.getListaHechos().get(0).getTitulo());
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

        Hecho hechoOriginal = new Hecho("Título", "Descripción vieja", "Incendio", new Coordenadas((float) -40.1, (float) -60.1), LocalDateTime.now().minusDays(1), Origen.DATASET);
        FuenteEstatica fuenteEstatica = new FuenteEstatica();
        fuenteEstatica.getListaHechos().add(hechoOriginal);

        Hecho hechoNuevo = new Hecho("Título", "Descripción NUEVA", "Incendio", new Coordenadas((float) -40.5, (float) -60.5), LocalDateTime.now(), Origen.DATASET);
        fuenteEstatica.getListaHechos().add(hechoNuevo); // simulamos que se "actualizó" como en un dataset real

        Coleccion coleccion = new Coleccion("Colección", "desc", "handle", fuenteEstatica, List.of(h -> true));
        Mockito.when(mockColeccionRepo.findAll()).thenReturn(List.of(coleccion));

        agregacionService.refrescarColecciones();

        List<Hecho> hechos = fuenteEstatica.getListaHechos().stream()
                .filter(h -> h.getTitulo().equalsIgnoreCase("Título"))
                .toList();

        assertEquals(1, hechos.size(), "Debe haber un solo hecho con ese título");
        assertEquals("Descripción NUEVA", hechos.get(0).getDescripcion(), "Debe ser la versión actualizada del hecho");
        assertEquals((float) -40.5, hechos.get(0).getCoordenadas().getLatitud(), 0.01);
    }

}
