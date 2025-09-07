package ar.edu.utn.frba.dds.metamapa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import ar.edu.utn.frba.dds.metamapa.services.impl.AgregacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class AggregationServiceTest {

  @MockitoBean
  private IColeccionesRepository mockColeccionRepo;

  @Autowired
  private IAgregacionService agregacionService;

  @Test
  void testRefrescarColecciones() {

    Hecho hecho = Hecho.builder()
        .titulo("Titulo")
        .descripcion("Descripción")
        .categoria("Incendio")
        .latitud(-34.6)
        .longitud(-58.4)
        .fechaAcontecimiento(LocalDateTime.now())
        .build();

    FuenteEstatica fuenteEstatica = new FuenteEstatica();
    fuenteEstatica.getHechos().add(hecho);

    Coleccion coleccion = new Coleccion("Incendios", "Hechos de incendios");
    coleccion.setCriterios(List.of());

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

    Hecho hechoOriginal = Hecho.builder()
        .titulo("Título")
        .descripcion("Descripción vieja")
        .categoria("Incendio")
        .latitud(-40.1)
        .longitud(-60.1)
        .fechaAcontecimiento(LocalDateTime.now().minusDays(1))
        .build();

    FuenteEstatica fuenteEstatica = new FuenteEstatica();
    fuenteEstatica.getHechos().add(hechoOriginal);

    Hecho hechoNuevo = Hecho.builder()
        .titulo("Título")
        .descripcion("Descripción NUEVA")
        .categoria("Incendio")
        .latitud(-40.5)
        .longitud(-60.5)
        .fechaAcontecimiento(LocalDateTime.now())
        .build();
    fuenteEstatica.getHechos().add(hechoNuevo); // simulamos que se "actualizó" como en un dataset real

    Coleccion coleccion = new Coleccion("Colección", "desc");
    coleccion.setCriterios(List.of());
    Mockito.when(mockColeccionRepo.findAll()).thenReturn(List.of(coleccion));

    agregacionService.refrescarColecciones();

    List<Hecho> hechos = fuenteEstatica.getHechos().stream()
        .filter(h -> h.getTitulo().equalsIgnoreCase("Título"))
        .toList();

    assertEquals(1, hechos.size(), "Debe haber un solo hecho con ese título");
    assertEquals("Descripción NUEVA", hechos.get(0).getDescripcion(), "Debe ser la versión actualizada del hecho");
    assertEquals(-40.5, hechos.get(0).getLatitud(), 0.01);
  }

}
