package ar.edu.utn.frba.dds.metamapa.models.repositories;
import ar.edu.utn.frba.dds.metamapa.models.entities.Fuente;

import java.util.List;

public interface IFuentesRepository {

    public List<Fuente> findAll();

    public void save(Fuente fuente);
}
