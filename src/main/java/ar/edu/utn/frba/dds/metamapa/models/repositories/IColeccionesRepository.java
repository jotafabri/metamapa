package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IColeccionesRepository extends JpaRepository<Coleccion, Long> {
  Coleccion findColeccionByHandle(String handle);

  boolean existsByHandle(String candidato);

  void deleteColeccionByHandle(String handle);
}