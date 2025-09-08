package ar.edu.utn.frba.dds.metamapa.models.repositories;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISolicitudesEliminacionRepository extends JpaRepository<SolicitudEliminacion, Long> {
  Long countByEsSpamTrue();
}
