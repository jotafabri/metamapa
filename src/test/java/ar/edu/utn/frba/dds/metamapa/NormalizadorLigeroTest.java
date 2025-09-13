package ar.edu.utn.frba.dds.metamapa;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.services.impl.NormalizadorLigero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NormalizadorLigeroTest {

    private NormalizadorLigero normalizadorLigero;

    @BeforeEach
    void setUp() {
        normalizadorLigero = new NormalizadorLigero();
    }

    @Test
    void testNormalizarTitulo() {
        String titulo = "  hólA MUndo!  ";
        String esperado = "Hola mundo";
        String normalizado = normalizadorLigero.normalizarTitulo(titulo);
        assertEquals(esperado, normalizado);
    }

    @Test
    void testNormalizarCategoria() {
        String categoria = "  INCENDIO fORESTAL!!!  ";
        String esperado = "Incendio forestal";
        String normalizado = normalizadorLigero.normalizarCategoria(categoria);
        assertEquals(esperado, normalizado);
    }

    @Test
    void testNormalizarHecho() {
        Hecho hecho = new Hecho();
        hecho.setTitulo("   robo en el banco!! ");
        hecho.setCategoria(" DELito cIBer  ");

        Hecho normalizado = normalizadorLigero.normalizar(hecho);

        assertEquals("Robo en el banco", normalizado.getTitulo());
        assertEquals("Delito ciber", normalizado.getCategoria());
    }

    @Test
    void testNormalizarTextoNulo() {
        assertNull(normalizadorLigero.normalizarTitulo(null));
        assertNull(normalizadorLigero.normalizarCategoria(null));
    }

    @Test
    void testNormalizarTextoVacio() {
        assertEquals("", normalizadorLigero.normalizarTitulo(""));
        assertEquals("", normalizadorLigero.normalizarCategoria(""));
    }
}