package ar.edu.utn.frba.dds.metamapa.models.entities;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.List;


public class FuenteEstatica extends Fuente {

  public void importarHechos(String ruta) {
   listaHechos = LectorCSV.main(ruta);
  }
}
