package ar.edu.utn.frba.dds.metamapa.models.repositories.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IFuentesRepository;
import org.springframework.stereotype.Repository;

@Repository
public class FuentesRepository implements IFuentesRepository {
    private List<Fuente> fuentes = new ArrayList();

    @Override
    public List<Fuente> findAll() {
        return fuentes;
    }

    @Override
    public void save(Fuente fuente) {
        if (!fuentes.contains(fuente)) {
            fuentes.add(fuente);
            if (fuente.getId() == null) fuente.setId((long) (this.fuentes.size() + 1));
            ;
        }
    }

    @Override
    public Fuente findById(Long id) {
        return this.fuentes
                .stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
