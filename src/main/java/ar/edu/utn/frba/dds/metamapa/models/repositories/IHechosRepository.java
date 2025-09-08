package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IHechosRepository extends JpaRepository<Hecho, Long> {

  // Lista solo los hechos que no fueron eliminados
  List<Hecho> findAllByEliminadoFalse();

  @Query("SELECT h FROM Hecho h WHERE h.ubicacion.provincia IS NULL AND h.eliminado = FALSE")
  List<Hecho> findAllSinProvincia();
}
