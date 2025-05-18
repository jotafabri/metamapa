package ar.edu.utn.frba.dds.metamapa;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FuenteDinamicaTest {
    private Coordenadas coordenadas;
    private Origen origen;
    private HechoBuilder hechoBuilder;
    private Contribuyente contribuyente;

    @BeforeEach
    void setup() {
        coordenadas = new Coordenadas((float) -34.6037, (float) -58.3816); // Coordenadas de Buenos Aires
        origen = Origen.CONTRIBUYENTE;
        hechoBuilder = new HechoBuilder("Titulo de prueba", "Descripcion de prueba", "Categoria de prueba", coordenadas, LocalDateTime.now(), origen);
        contribuyente = new Contribuyente("Juan", "Perez", 30, false);
    }

    @Test
    void testCreacionHecho() {
        Hecho hecho = hechoBuilder.conContribuyente(contribuyente).build();
        assertNotNull(hecho);
        assertEquals("Titulo de prueba", hecho.getTitulo());
        assertEquals("Descripcion de prueba", hecho.getDescripcion());
        assertEquals("Categoria de prueba", hecho.getCategoria());
        assertEquals(coordenadas, hecho.getCoordenadas());
        assertEquals(origen, hecho.getOrigen());
    }

    @Test
    void testAgregarEtiqueta() {
        Hecho hecho = hechoBuilder.conContribuyente(contribuyente).build();
        hecho.agregarEtiqueta("importante");
        hecho.agregarEtiqueta("urgente");
        List<String> etiquetas = hecho.getEtiquetas();
        assertEquals(2, etiquetas.size());
        assertTrue(etiquetas.contains("importante"));
        assertTrue(etiquetas.contains("urgente"));
    }

    @Test
    void testActualizarHecho() {
        Hecho hecho = hechoBuilder.conContribuyente(contribuyente).build();
        Hecho nuevoHecho = new HechoBuilder("Nuevo Titulo", "Nueva Descripcion", "Nueva Categoria", coordenadas, LocalDateTime.now(), origen)
                .conContribuyente(contribuyente)
                .agregarEtiqueta("nuevo")
                .build();
        hecho.actualizarHecho(nuevoHecho);
        assertEquals("Nueva Descripcion", hecho.getDescripcion());
        assertEquals("Nueva Categoria", hecho.getCategoria());
        assertTrue(hecho.getEtiquetas().contains("nuevo"));
    }
//TODO: Falla
    @Test
    void testHechoNoEditable() {
        Contribuyente contribuyente = new Contribuyente("Juan", "Perez", 30, false);
        Hecho hecho = new HechoBuilder("Titulo", "Descripcion", "Categoria", coordenadas, LocalDateTime.now().minusDays(8), origen)
                .conContribuyente(contribuyente)
                .build();
        assertFalse(hecho.esEditable());
    }

    @Test
    void testHechoEditable() {
        Hecho hecho = hechoBuilder.conContribuyente(contribuyente).conLimiteDiasEdicion(10).build();
        assertTrue(hecho.esEditable());
    }

    @Test
    void testContribuyenteAnonimoNoEditable() {
        Contribuyente anonimo = new Contribuyente("Anonimo", "", 0, true);
        Hecho hecho = hechoBuilder.conContribuyente(anonimo).build();
        assertFalse(hecho.esEditable());
    }
}
