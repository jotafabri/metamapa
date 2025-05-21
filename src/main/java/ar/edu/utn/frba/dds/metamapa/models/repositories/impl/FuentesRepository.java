package ar.edu.utn.frba.dds.metamapa.models.repositories.impl;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IFuentesRepository;

public class FuentesRepository implements IFuentesRepository{
    private List<Fuente> fuentes;

    @Override
    public List<Fuente> findAll(){
        return fuentes;
    }

    @Override
    public void save(Fuente fuente){
        if (!fuentes.contains(fuente)) fuentes.add(fuente);
    }
}
