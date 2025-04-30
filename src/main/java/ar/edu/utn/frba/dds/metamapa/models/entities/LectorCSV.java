package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LectorCSV {

  private BufferedReader lector;

  public List<Hecho> inicializarLectura(String rutaCSV) {
    if (!rutaCSV.endsWith(".csv")) {
      return Collections.emptyList();
    }

    List<Hecho> hechos = new ArrayList<>();
    File archivo = new File(rutaCSV);

    if (!archivo.exists()) {
      return Collections.emptyList();
    }

    // Si o si tiene que haber un try catch aca para que pueda leer el archivo...
    // Piensa en el peor caso donde no hay un archivo cargado
    // En ese caso devolveria una lista vacia
    try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
      String cabecera = lector.readLine();
      lector.lines().forEach(linea -> {
        if (linea.trim().isEmpty()) {
          return;
        }
        Hecho hecho = parsearHecho(linea);
        //habría que hacerlo para que respete los que están previamente cargados en la fuente
        if (!existeHechoConMismoTitulo(hecho, hechos)) {
          hechos.add(hecho);
        }
      });
    } catch (IOException e) {
      return Collections.emptyList();
    }

    return hechos;
  }

  private Hecho parsearHecho(String linea) {
    //String[] campos = linea.split(",");
    List<String> campos = splitCSV(linea);

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

    return new Hecho(titulo, descripcion, categoria, new Coordenadas(lat, lon), fechaAcontecimiento, Origen.DATASET);
  }

  public static List<String> splitCSV(String linea) {
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

  public boolean existeHechoConMismoTitulo(Hecho hecho, List<Hecho> hechos) {
    for (Hecho h : hechos) {
      if (h.getTitulo().equalsIgnoreCase(hecho.getTitulo())) {
        h.setDescripcion(hecho.getDescripcion());
        h.setCategoria(hecho.getCategoria());
        h.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
        h.setFechaCarga(hecho.getFechaCarga());
        h.setOrigen(hecho.getOrigen());
        return true;
      }
    }
    return false;
  }
}
