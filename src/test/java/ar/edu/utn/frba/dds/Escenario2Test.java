package ar.edu.utn.frba.dds;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Escenario2Test {
    private LectorCSV lector = new LectorCSV();
    private List<String> rutas = new ArrayList<>();

    @BeforeEach
    public void init() {
        rutas.add("src/test/desastres_naturales_argentina.csv");
        rutas.add("src/test/desastres_sanitarios_contaminacion_argentina.csv");
        rutas.add("src/test/desastres_tecnologicos_argentina.csv");
    }

    @Test
    @DisplayName("Escenario 2: Importación de hechos por csv")
    public void VisualizoColeccion() {
        for(String ruta : rutas) {
            System.out.printf("Importando ruta: %s%n",ruta);
            FuenteEstatica fuente = new FuenteEstatica();
            fuente.importarHechos(lector, ruta);
            Assertions.assertFalse(fuente.getListaHechos().isEmpty());
            System.out.printf("Fuente %d cargada correctamente",rutas.indexOf(ruta)+1);
            // fuente.listarHechos();
            System.out.println("\n--------------------------------------------");
        }
    }
}
