package ar.edu.utn.frba.dds.metamapa.models.entities;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.List;


public class FuenteEstatica extends Fuente {

  public void importarHechos(LectorCSV lector, String ruta) {
    try {
      if (lector.inicializarLectura(ruta)) {
        List<String> linea;
        while ((linea = lector.conseguirSiguienteLinea()) != null) {
          Hecho hecho = parsearHecho(linea);
          this.agregarHecho(hecho);

        }
      }
    } catch (IOException e) {
      throw new RuntimeException("No se pudo hacer la lectura del CSV.");
    }
  }

  private Hecho parsearHecho(List<String> campos) {
    if (campos.size() < 6) {
      return null;
    }

    String titulo = campos.get(0).replaceAll("^\"|\"$", ""); // quitar comillas
    String descripcion = campos.get(1).replaceAll("^\"|\"$", "");
    String categoria = campos.get(2); //Todavía no está resuelto esto de las categorías
    float lat = Float.parseFloat(campos.get(3));
    float lon = Float.parseFloat(campos.get(4));
    LocalDate fecha = LocalDate.parse(campos.get(5), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    LocalDateTime fechaAcontecimiento = fecha.atStartOfDay();

    return new HechoBuilder(titulo, descripcion, categoria, new Coordenadas(lat, lon), fechaAcontecimiento, Origen.DATASET)
            .build();
  }

}
