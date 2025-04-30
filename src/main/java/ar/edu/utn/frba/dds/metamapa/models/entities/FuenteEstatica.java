package ar.edu.utn.frba.dds.metamapa.models.entities;


import java.util.Optional;
import java.util.List;


public class FuenteEstatica extends Fuente {

  public void importarHechos(LectorCSV lector, String ruta) {
    <Optional><List> importacion = lector.obtenerHechos(ruta);
    //HAY QUE PROFUNDIZAR, por los duplicados y eso, son temas
    //podemos usar la funcion agregarHechos de dinamica para evitar los duplicados
  }
}
