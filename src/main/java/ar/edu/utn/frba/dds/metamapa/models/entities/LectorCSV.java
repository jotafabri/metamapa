package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.io.*;
import java.net.URL;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;

public class LectorCSV {
  //revisar necesidad de ser clase y no interfaz
  @SneakyThrows //eso lo que hace internamente es el try catch que pusimos en fuente estática
  public List<Hecho> leer(String rutaCSV) {
    Reader reader;
    if (rutaCSV.startsWith("http://")) {
      URL url = new URL(rutaCSV);
      reader = new InputStreamReader(url.openStream());
    } else {
      reader = new FileReader(rutaCSV);
    }

    CsvToBean<Hecho> csvToBean = new CsvToBeanBuilder<Hecho>(reader)
        .withType(Hecho.class)
        .withIgnoreLeadingWhiteSpace(true)
        .build();

    List<Hecho> hechos = csvToBean.parse();
    reader.close();
    return hechos;
  }
}
