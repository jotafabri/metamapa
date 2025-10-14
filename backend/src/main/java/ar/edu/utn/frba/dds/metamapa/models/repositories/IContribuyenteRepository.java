package ar.edu.utn.frba.dds.metamapa.models.repositories;

import ar.edu.utn.frba.dds.metamapa.models.entities.Usuario;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Contribuyente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IContribuyenteRepository extends JpaRepository<Contribuyente, Long> {
  Usuario findByUsuario_Id(Long usuarioId); 
}
