package ar.edu.utn.frba.dds;

public class FuenteEstatica extends Fuente {

    public void importarHechos(LectorCSV lector, String ruta){
        this.listaHechos = lector.obtenerHechos(ruta);
        //HAY QUE PROFUNDIZAR, por los duplicados y eso, son temas
        //podemos usar la funcion agregarHechos de dinamica para evitar los duplicados
    }
}