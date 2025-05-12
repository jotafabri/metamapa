package ar.edu.utn.frba.dds.metamapa.models.repositories;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Objects;

public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {

    public void saveDistinto(Coleccion coleccion);
}

