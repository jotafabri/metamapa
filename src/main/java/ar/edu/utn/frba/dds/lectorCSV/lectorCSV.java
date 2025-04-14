package ar.edu.utn.frba.dds.lectorCSV;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class lectorCSV {

    private final File archivo;
    private final Logger logger = Logger.getLogger(LectorCSV.class.getName());
    private final List<Hecho> hechos = new ArrayList<>();

    public LectorCSV(String rutaCSV) {
        if (rutaCSV.endsWith(".csv")) {
            this.archivo = new File(rutaCSV);
        } else {
            throw new IllegalArgumentException("La ruta del archivo debe tener extensión .csv.");
        }
    }

    public List<Hecho> obtenerHechos() {
        if (archivo.exists()) {
            try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
                String cabecera = lector.readLine();
                lector.lines().forEach(linea -> {
                    try {
                        hechos.add(parsearHecho(linea));
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Error al procesar la línea " + linea + ".", e);
                    }
                });
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error al leer el archivo CSV", ex);
            }
        } else {
            throw new RuntimeException("El archivo CSV no existe en la ruta indicada.");
        }
        return hechos;
    }

    private Hecho parsearHecho(String linea) {
        String[] campos = linea.split(";");
        if (campos.length < 6) {
            throw new IllegalArgumentException("Hecho incompleto: " + linea);
        }

        String titulo = campos[0];
        String descripcion = campos[1];
        String categoria = campos[2];
        double latitud = Double.parseDouble(campos[3]);
        double longitud = Double.parseDouble(campos[4]);
        LocalDate fechaAcontecimiento = LocalDate.parse(campos[5], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate fechaCarga = LocalDate.now();

        return new Hecho(titulo, descripcion, categoria, latitud, longitud, fechaAcontecimiento, fechaCarga);
    }
}
