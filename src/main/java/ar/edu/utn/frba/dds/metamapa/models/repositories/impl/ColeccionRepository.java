package ar.edu.utn.frba.dds.metamapa.models.repositories.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Repository
public abstract class ColeccionRepository implements IColeccionRepository {

    private List<Coleccion> colecciones;


    public ColeccionRepository(List<Coleccion> colecciones) {
        this.colecciones = colecciones;
    }


    @Override
    public void saveDistinto(Coleccion coleccion) {
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

