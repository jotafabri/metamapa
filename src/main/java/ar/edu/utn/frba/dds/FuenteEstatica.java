package ar.edu.utn.frba.dds;

public class FuenteEstatica extends Fuente {

    public void importarHechos(LectorCSV lector, String ruta){
        this.listaHechos = lector.obtenerHechos(ruta);
    }

}
