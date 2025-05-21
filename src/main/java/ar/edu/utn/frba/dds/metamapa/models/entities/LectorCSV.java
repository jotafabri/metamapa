package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class LectorCSV {
  public static List<Hecho> main(String rutaCSV) throws FileNotFoundException {
    FileReader reader = new FileReader(rutaCSV);

    CsvToBean<Hecho> csvToBean = new CsvToBeanBuilder<Hecho>(reader)
        .withType(Hecho.class)
        .withIgnoreLeadingWhiteSpace(true)
        .build();

    List<Hecho> hechos = csvToBean.parse();
    return hechos;
  }

  private BufferedReader lector;

  public Boolean inicializarLectura(String rutaCSV) throws IOException {
    if (!rutaCSV.endsWith(".csv")) {
      return false;
    }
    File archivo = new File(rutaCSV);

    if (!archivo.exists()) {
      return false;
    }
    this.lector = new BufferedReader(new FileReader(archivo));
    lector.readLine();

    return true;
  }

  public List<String> conseguirSiguienteLinea() {
    try {
      String linea = lector.readLine(); // Devuelve null si llegó al final
      return splitCSV(linea);

    } catch (IOException e) {
      throw new RuntimeException("Error al leer la siguiente línea del archivo", e);
    }
  }


  public static List<String> splitCSV(String linea) {
    if (linea == null) {
      return null;
    }
    List<String> campos = new ArrayList<>();
    StringBuilder actual = new StringBuilder();
    boolean dentroDeComillas = false;

    for (int i = 0; i < linea.length(); i++) {
      char c = linea.charAt(i);

      if (c == '"') {
        dentroDeComillas = !dentroDeComillas; // alterna entre abierto/cerrado
      } else if (c == ',' && !dentroDeComillas) {
        campos.add(actual.toString().trim());
        actual.setLength(0);
      } else {
        actual.append(c);
      }
    }
    campos.add(actual.toString().trim()); // último campo
    return campos;
  }


}
