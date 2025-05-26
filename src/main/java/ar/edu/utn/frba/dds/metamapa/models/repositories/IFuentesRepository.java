package ar.edu.utn.frba.dds.metamapa.models.repositories;
import ar.edu.utn.frba.dds.metamapa.models.entities.Fuente;

import java.util.List;

public interface IFuentesRepository {

    List<Fuente> findAll();

    void save(Fuente fuente);

    public Fuente findById(Long id);
}
