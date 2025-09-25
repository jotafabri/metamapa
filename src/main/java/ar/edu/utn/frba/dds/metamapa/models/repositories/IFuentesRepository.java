package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFuentesRepository extends JpaRepository<Fuente, Long> {
  Fuente findFuenteById(Long idFuente);
}
