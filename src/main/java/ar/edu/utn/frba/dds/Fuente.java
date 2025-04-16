package ar.edu.utn.frba.dds;

import java.io.File;
import java.util.List;

public class Fuente {
   /*     - archivo: File
    - listaHechos: List<Hecho>
    + importarHechos()
    + eliminarHecho()*/

    private File archivo;
    private List<Hecho> listaHechos;


    public List<Hecho> getListaHechos() {
        return listaHechos;
    }
}
