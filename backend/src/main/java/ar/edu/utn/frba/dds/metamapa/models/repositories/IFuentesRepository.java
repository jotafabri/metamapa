package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteDinamica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IFuentesRepository extends JpaRepository<Fuente, Long> {
  Fuente findFuenteById(Long idFuente);

  Optional<FuenteDinamica> findFirstBy();

  List<Fuente> findAllByIdIn(Collection<Long> ids);
}
