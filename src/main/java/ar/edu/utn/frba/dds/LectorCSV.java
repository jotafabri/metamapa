package ar.edu.utn.frba.dds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LectorCSV {

    private File archivo;
    //private final Logger logger = Logger.getLogger(LectorCSV.class.getName());
    private List<Hecho> hechos = new ArrayList<>();


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
                        if (linea.trim().isEmpty()) {
                            System.out.println("Línea vacía ignorada");
                            return;
                        }
                        hechos.add(parsearHecho(linea));
                    } catch (Exception e) {
                        System.err.println("Error al procesar línea: " + linea);
                        e.printStackTrace();
                    }
                });
            } catch (IOException ex) {
                System.out.println(0);
                //logger.log(Level.SEVERE, "Error al leer el archivo CSV", ex);
            }
        } else {
            System.out.println(0);
            throw new RuntimeException("El archivo CSV no existe en la ruta indicada.");
        }
        return hechos;
    }

    private Hecho parsearHecho(String linea) {
        //String[] campos = linea.split(",");
        List<String> campos = splitCSV(linea);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (campos.size() < 6) {
            System.out.println(0);
            throw new IllegalArgumentException("Hecho incompleto: " + linea);
        }

        String titulo = campos.get(0).replaceAll("^\"|\"$", ""); // quitar comillas
        String descripcion = campos.get(1).replaceAll("^\"|\"$", "");
        String categoria = campos.get(2);
        float lat = Float.parseFloat(campos.get(3));
        float lon = Float.parseFloat(campos.get(4));
        LocalDate fecha = LocalDate.parse(campos.get(5), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDateTime fechaAcontecimiento = fecha.atStartOfDay();
        LocalDateTime fechaCarga = LocalDateTime.now();

        return new Hecho(titulo, descripcion, categoria, new Coordenadas(lat, lon), fechaAcontecimiento, fechaCarga, Origen.dataset);
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

}
