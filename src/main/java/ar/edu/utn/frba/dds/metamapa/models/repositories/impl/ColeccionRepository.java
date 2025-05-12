package ar.edu.utn.frba.dds.metamapa.models.repositories.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ColeccionRepository implements IColeccionRepository {

    private List<Coleccion> colecciones;


    public ColeccionRepository(List<Coleccion> colecciones) {
        this.colecciones = colecciones;
    }
    @Override
    public List<Coleccion> findAll(){return colecciones;}

    @Override
    public void save(Coleccion coleccion) {
        boolean i = true;
        for (Coleccion c : colecciones) {
            if(Objects.equals(c.getHandle(), coleccion.getHandle())){
                i = false;
                c.actualizarColeccion(coleccion);
            }
        }
        if(i){
            colecciones.add(coleccion);
        }
    }
}
}
