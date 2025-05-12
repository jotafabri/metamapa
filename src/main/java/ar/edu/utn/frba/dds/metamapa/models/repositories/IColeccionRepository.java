package ar.edu.utn.frba.dds.metamapa.repositories;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {
}

