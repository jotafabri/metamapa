package ar.edu.utn.frba.dds;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CSVTest {

    private LectorCSV lector;
    private String ruta = "src/test/desastres_naturales_argentina.csv";
    private List<Hecho> listaHechos;

    @BeforeEach
    public void init(){
        this.lector = new LectorCSV(ruta);
    }

    @Test
    @DisplayName("La lista de hechos no esta vacía")
    public void ListaNoVacia() {
        this.listaHechos = this.lector.obtenerHechos();

        Assertions.assertFalse(this.listaHechos.isEmpty());

        for (Hecho elemento : this.listaHechos) {
            System.out.println(elemento.getCategoria());
        }
    }
}
