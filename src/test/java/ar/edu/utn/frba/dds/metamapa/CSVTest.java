package ar.edu.utn.frba.dds.metamapa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ar.edu.utn.frba.dds.metamapa.models.entities.FiltroFechaAcontecimiento;
import ar.edu.utn.frba.dds.metamapa.models.entities.Filtro;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.LectorCSV;
import org.junit.jupiter.api.BeforeEach;


import java.util.List;

public class CSVTest {

  private LectorCSV lector;
  private String ruta = "src/test/desastres_naturales_argentina.csv";
  private List<Hecho> listaHechos;
  private List<Hecho> listaFiltrada;
  private List<Filtro> listaCriterios;

  @BeforeEach
  public void init() {
    this.lector = new LectorCSV();
    this.listaCriterios = new ArrayList<>();
    Filtro critFecha = new FiltroFechaAcontecimiento(
        LocalDate.parse("01/01/2013", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(),
        LocalDateTime.now());
    this.listaCriterios.add(critFecha);
  }
/*
  @Test
  @DisplayName("La lista de hechos no esta vacía")
  public void ListaNoVacia() {
    this.listaHechos = this.lector.obtenerHechos(ruta);

    Assertions.assertFalse(this.listaHechos.isEmpty());

    for (Hecho elemento : this.listaHechos) {
      System.out.println(elemento.getFechaAcontecimiento());
    }
  }

  @Test
  @DisplayName("Cargo y visualizo una colección")
  public void VisualizoColeccion() {
    FuenteEstatica fuente = new FuenteEstatica();
    fuente.importarHechos(lector, ruta);
    Coleccion coleccion = new Coleccion("Coleccion de Prueba", "Coleccion de prueba descripcion",
        fuente, this.listaCriterios);
    Assertions.assertFalse(coleccion.darHechos().isEmpty());
    coleccion.navegar(null);
  }

  @Test
  public void FiltroListaHechos() {
    this.listaHechos = this.lector.obtenerHechos(ruta);
    Assertions.assertFalse(this.listaHechos.isEmpty());
    System.out.println(listaFiltrada);
  }*/

/*  @Test
  @DisplayName("Test de nuevo lector")
  public void NuevoLectorTest() {
    try {
      var lectorNuevo = LectorCSV.leer("static/csv/desastres_naturales_argentina.csv");
      Assertions.assertFalse(lectorNuevo.isEmpty());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }*/
}
