package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Estadistica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadisticasRepository extends JpaRepository<Estadistica, Long> {
}