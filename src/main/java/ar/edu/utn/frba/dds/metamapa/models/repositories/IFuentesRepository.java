package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;

public interface IFuentesRepository {

  List<Fuente> findAll();

  void save(Fuente fuente);

  public Fuente findById(Long id);
}
