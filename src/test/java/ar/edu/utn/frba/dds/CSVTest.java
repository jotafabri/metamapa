package ar.edu.utn.frba.dds;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;

public class CSVTest {

  private LectorCSV lector;
  private String ruta = "src/test/desastres_naturales_argentina.csv";
  private Fuente fuente;
  private List<Hecho> listaHechos;
  private List<Hecho> listaFiltrada;
  private List<CriterioPertenencia> listaCriterios;

  @BeforeEach
  public void init() {
    this.lector = new LectorCSV();
    this.listaCriterios = new ArrayList<>();
    CriterioPertenencia critFecha = new CriterioFechaAcontecimiento(
        LocalDate.parse("01/01/2013", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(),
        LocalDateTime.now());
    this.listaCriterios.add(critFecha);
  }

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
    this.fuente = new Fuente(ruta);
    fuente.importarHechos(lector);
    Coleccion coleccion = new Coleccion("Coleccion de Prueba", "Coleccion de prueba descripcion",
        fuente, this.listaCriterios);
    coleccion.importarHechos();
    Assertions.assertFalse(coleccion.getListaHechos().isEmpty());
    coleccion.navegar(null);
  }

  @Test
  public void FiltroListaHechos() {
    this.listaHechos = this.lector.obtenerHechos(ruta);
    Assertions.assertFalse(this.listaHechos.isEmpty());
    System.out.println(listaFiltrada);
  }
}
