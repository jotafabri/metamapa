package ar.edu.utn.frba.dds.metamapa;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Ubicacion;
import ar.edu.utn.frba.dds.metamapa.services.normalizador.MapeadorTexto;
import ar.edu.utn.frba.dds.metamapa.services.normalizador.NormalizadorFuerte;
import ar.edu.utn.frba.dds.metamapa.services.normalizador.NormalizadorLigero;
import ar.edu.utn.frba.dds.metamapa.services.normalizador.ValidadorFechas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NormalizadorFuerteTest {

    private NormalizadorFuerte normalizadorFuerte;

    @BeforeEach
    void setUp() throws IOException {
        NormalizadorLigero normalizadorLigero = new NormalizadorLigero();

        // Archivos JSON simulados
        MapeadorTexto mapeadorCategorias = new MapeadorTexto("src/test/resources/diccionario_categorias.json");
        MapeadorTexto mapeadorUbicaciones = new MapeadorTexto("src/test/resources/diccionario_ubicaciones.json");

        ValidadorFechas validadorFechas = new ValidadorFechas();

        normalizadorFuerte = new NormalizadorFuerte(normalizadorLigero, mapeadorCategorias, mapeadorUbicaciones, validadorFechas);
    }

    @Test
    void testNormalizacionLigeraTitulo() {
        Hecho hecho = new Hecho();
        hecho.setTitulo("  incendio FORESTAL!! ");
        hecho = normalizadorFuerte.normalizar(hecho);

        assertEquals("Incendio forestal", hecho.getTitulo());
    }

    @Test
    void testNormalizacionFuerteCategoria() {
        Hecho hecho = new Hecho();
        hecho.setCategoria("incendio boscoso");
        hecho = normalizadorFuerte.normalizar(hecho);

        // Suponiendo que el diccionario mapea "incendio boscoso" -> "Incendio Forestal"
        assertEquals("Incendio Forestal", hecho.getCategoria());
    }

    @Test
    void testNormalizacionUbicacionTexto() {
        Hecho hecho = new Hecho();
        Ubicacion u = new Ubicacion("caba", "caba", "argentina");
        hecho.setUbicacion(u);

        hecho = normalizadorFuerte.normalizar(hecho);

        // Suponiendo diccionario normaliza a mayúsculas iniciales
        assertEquals("Caba", hecho.getUbicacion().getLocalidad());
        assertEquals("Caba", hecho.getUbicacion().getProvincia());
        assertEquals("Argentina", hecho.getUbicacion().getPais());
    }

    @Test
    void testNormalizacionUbicacionCoordenadas() {
        Hecho hecho = new Hecho();
        hecho.setLatitud(-34.6037);
        hecho.setLongitud(-58.3816);

        Ubicacion u = new Ubicacion("algún lugar", "alguna provincia", "argentina");
        hecho.setUbicacion(u);

        hecho = normalizadorFuerte.normalizar(hecho);

        // Como lat/lon existen, no modifica localidad/provincia/pais, solo aplica normalización ligera si acaso
        assertEquals("Algún lugar", hecho.getUbicacion().getLocalidad());
        assertEquals("Alguna provincia", hecho.getUbicacion().getProvincia());
        assertEquals("Argentina", hecho.getUbicacion().getPais());

        // Latitud y longitud redondeadas
        assertEquals(-34.6037, hecho.getLatitud());
        assertEquals(-58.3816, hecho.getLongitud());
    }

    @Test
    void testFechas() {
        Hecho hecho = new Hecho();
        LocalDateTime fecha = LocalDateTime.of(2025, 9, 13, 10, 0);
        hecho.setFechaAcontecimiento(fecha);
        hecho.setFechaCarga(fecha);

        hecho = normalizadorFuerte.normalizar(hecho);

        assertEquals(fecha, hecho.getFechaAcontecimiento());
        assertEquals(fecha, hecho.getFechaCarga());
    }

    @Test
    void testLatitudFueraDeRango() {
        Hecho hecho = new Hecho();
        hecho.setLatitud(200.0); // fuera de rango
        hecho.setLongitud(50.0);

        assertThrows(IllegalArgumentException.class, () -> normalizadorFuerte.normalizar(hecho));
    }

    @Test
    void testLongitudFueraDeRango() {
        Hecho hecho = new Hecho();
        hecho.setLatitud(50.0);
        hecho.setLongitud(-200.0); // fuera de rango

        assertThrows(IllegalArgumentException.class, () -> normalizadorFuerte.normalizar(hecho));
    }
}
