package ar.edu.utn.frba.dds.metamapa;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FuenteDinamicaTest {
    //private Coordenada coordenada;
    private HechoBuilder hechoBuilder;
    private Contribuyente contribuyente;
    //private Categoria categoria;

    @BeforeEach
    void setup() {
        //coordenada = new Coordenada((float) -34.6037, (float) -58.3816); // Coordenadas de Buenos Aires
        //categoria = new Categoria("Categoria de prueba");
        hechoBuilder = new HechoBuilder("Titulo de prueba", "Descripcion de prueba", "Categoria de prueba", -12.3251, -12.2445, LocalDateTime.now());
        contribuyente = new Contribuyente("Juan", "Perez", 30, false);
    }

    @Test
    void testCreacionHecho() {
        Hecho hecho = hechoBuilder.conContribuyente(contribuyente).build();
        assertNotNull(hecho);
        assertEquals("Titulo de prueba", hecho.getTitulo());
        assertEquals("Descripcion de prueba", hecho.getDescripcion());
        assertEquals("Categoria de prueba", hecho.getCategoria());
        //assertEquals(coordenada, hecho.getCoordenada());

        //assertEquals(origen, hecho.getOrigen());
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
        //Categoria nuevaCategoria = new Categoria("Nueva Categoria");
        Hecho nuevoHecho = new HechoBuilder("Nuevo Titulo", "Nueva Descripcion", "Categoria Nueva", -12.3251, -12.2445, LocalDateTime.now())
                .conContribuyente(contribuyente)
                .agregarEtiqueta("nuevo")
                .build();
        hecho.actualizarHecho(nuevoHecho);
        assertEquals("Nueva Descripcion", hecho.getDescripcion());
        assertEquals("Nueva Categoria", hecho.getCategoria());
        assertTrue(hecho.getEtiquetas().contains("nuevo"));
    }

    @Test
    void testHechoNoEditable() {
       // Categoria categoria = new Categoria("Categoria");
        //Coordenada coordenadas = new Coordenada((float) -34.60, (float) -58.38);
        Hecho hecho = new HechoBuilder("Titulo", "Descripcion", "Categoria de prueba", -12.3251, -12.2445, LocalDateTime.now().minusDays(10))
                .conFechaCarga(LocalDateTime.now().minusDays(10))
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
