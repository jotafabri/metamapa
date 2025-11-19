package ar.edu.utn.frba.dds.metamapa.models.repositories;

import ar.edu.utn.frba.dds.metamapa.models.entities.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
  Optional<Usuario> findByEmail(String email);
  boolean existsByEmail(String email);
}