package ar.edu.utn.frba.dds.metamapa.models.entities.utils;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;

public class LectorCSV {

  @SneakyThrows
  public List<Hecho> leer(String rutaCSV) {
    Reader reader;

    if (rutaCSV.startsWith("http://") || rutaCSV.startsWith("https://")) {
      // Mantener lectura vía HTTP
      URL url = new URL(rutaCSV);
      reader = new InputStreamReader(url.openStream());
    } else {
      // Primero intentar leer desde resources
      InputStream is = getClass().getClassLoader().getResourceAsStream(rutaCSV);
      if (is != null) {
        reader = new InputStreamReader(is);
      } else {
        // Si no existe en resources, intentar ruta absoluta del sistema de archivos
        File file = new File(rutaCSV);
        if (!file.exists()) {
          throw new RuntimeException("Archivo CSV no encontrado en resources ni en filesystem: " + rutaCSV);
        }
        reader = new FileReader(file);
      }
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
