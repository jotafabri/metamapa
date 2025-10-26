package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ISolicitudesEliminacionRepository extends JpaRepository<SolicitudEliminacion, Long> {
  Long countByEsSpamTrue();

  @Query("SELECT s FROM SolicitudEliminacion s WHERE s.esSpam = FALSE AND LOWER(s.estado) = 'pendiente' ")
  List<SolicitudEliminacion> findAllPendientes();
}
