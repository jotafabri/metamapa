package ar.edu.utn.frba.dds.metamapa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Contribuyente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FuenteDinamicaTest {
  //private Coordenada coordenada;
  private Hecho.HechoBuilder hechoBuilder;
  private Contribuyente contribuyente;
  //private Categoria categoria;

  @BeforeEach
  void setup() {
    //coordenada = new Coordenada((float) -34.6037, (float) -58.3816); // Coordenadas de Buenos Aires
    //categoria = new Categoria("Categoria de prueba");
    hechoBuilder = Hecho.builder()
        .titulo("Titulo de prueba")
        .descripcion("Descripcion de prueba")
        .categoria("Categoria de prueba")
        .latitud(-12.3251)
        .longitud(-12.2445)
        .fechaAcontecimiento(LocalDateTime.of(2023, 12, 5, 14, 30));

    contribuyente = new Contribuyente("Juan", "Perez", LocalDate.of(2001,2,3), false);
  }

  @Test
  void testCreacionHecho() {
    Hecho hecho = hechoBuilder.contribuyente(contribuyente).build();
    assertNotNull(hecho);
    assertEquals("Titulo de prueba", hecho.getTitulo());
    assertEquals("Descripcion de prueba", hecho.getDescripcion());
    assertEquals("Categoria de prueba", hecho.getCategoria());
    //assertEquals(coordenada, hecho.getCoordenada());

    //assertEquals(origen, hecho.getOrigen());
  }

  @Test
  void testAgregarEtiqueta() {
    Hecho hecho = hechoBuilder.contribuyente(contribuyente).build();
    hecho.agregarEtiqueta("importante");
    hecho.agregarEtiqueta("urgente");
    List<String> etiquetas = hecho.getEtiquetas();
    assertEquals(2, etiquetas.size());
    assertTrue(etiquetas.contains("importante"));
    assertTrue(etiquetas.contains("urgente"));
  }

  @Test
  void testActualizarHecho() {
    Hecho hecho = hechoBuilder
        .contribuyente(contribuyente)
        .limiteDiasEdicion((long) 7)
        .build();
    //Categoria nuevaCategoria = new Categoria("Nueva Categoria");

    Hecho nuevoHecho = Hecho.builder()
        .titulo("Nuevo Titulo")
        .descripcion("Nueva Descripcion")
        .categoria("Nueva Categoria")
        .latitud(-12.3251)
        .longitud(-12.2445)
        .fechaAcontecimiento(LocalDateTime.of(2023, 12, 5, 14, 30))
        .contribuyente(contribuyente)
        .limiteDiasEdicion((long) 7) // Manual para el test, cuando un usuario cree un hecho se setea automaticamente
        .build();

    nuevoHecho.agregarEtiqueta("nuevo");

    hecho.actualizarHecho(nuevoHecho);
    assertEquals("Nueva Descripcion", hecho.getDescripcion());
    assertEquals("Nueva Categoria", hecho.getCategoria());
    assertTrue(hecho.getEtiquetas().contains("nuevo"));
  }

  @Test
  void testHechoNoEditable() {
    // Categoria categoria = new Categoria("Categoria");
    //Coordenada coordenadas = new Coordenada((float) -34.60, (float) -58.38);

    Hecho hecho = Hecho.builder()
        .titulo("Titulo")
        .descripcion("Descripción")
        .categoria("Categoria de prueba")
        .latitud(-12.3251)
        .longitud(-12.2445)
        .fechaCarga(LocalDateTime.now().minusDays(10))
        .contribuyente(contribuyente)
        .limiteDiasEdicion((long) 7)
        .build();

    assertFalse(hecho.esEditable());
  }


  @Test
  void testHechoEditable() {
    Hecho hecho = hechoBuilder
        .contribuyente(contribuyente)
        .fechaCarga(LocalDateTime.now().minusDays(9))
        .limiteDiasEdicion(10L).build();

    assertTrue(hecho.esEditable());
  }

  @Test
  void testContribuyenteAnonimoNoEditable() {
    Contribuyente anonimo = Contribuyente.buildAnonimo();
    Hecho hecho = hechoBuilder.contribuyente(anonimo).build();
    assertFalse(hecho.esEditable());
  }
}
