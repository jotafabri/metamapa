package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IHechosRepository extends JpaRepository<Hecho, Long> {

  // Lista solo los hechos que fueron aceptados
  @Query("SELECT h FROM Hecho h WHERE h.eliminado = FALSE AND LOWER(h.estado) = 'aceptada' ")
  List<Hecho> findAllAceptados();

  // Lista solo los hechos que no fueron eliminados
  List<Hecho> findAllByEliminadoFalse();

  @Query("SELECT h FROM Hecho h WHERE h.ubicacion.provincia IS NULL AND h.eliminado = FALSE AND LOWER(h.estado) = 'aceptada' ")
  List<Hecho> findAllSinProvincia();

  @Query(value = "SELECT h.provincia FROM hecho h " +
      "JOIN coleccion_hecho ch ON h.id = ch.hecho_id " +
      "JOIN coleccion c ON ch.coleccion_id = c.id " +
      "WHERE c.handle = :coleccionHandle AND h.eliminado = false " +
      "AND h.provincia IS NOT NULL " +
      "GROUP BY h.provincia " +
      "ORDER BY COUNT(*) DESC LIMIT 1", nativeQuery = true)
  String findProvinciaConMasHechosPorColeccion(@Param("coleccionHandle") String coleccionHandle);

  @Query("SELECT h.categoria FROM Hecho h WHERE LOWER(h.estado) = 'aceptada' " +
      "GROUP BY h.categoria " +
      "ORDER BY COUNT(h) DESC LIMIT 1")
  String findCategoriaConMasHechos();

  @Query("SELECT EXTRACT(HOUR FROM h.fechaAcontecimiento) AS hora FROM Hecho h " +
      "WHERE h.categoria = :categoria AND h.eliminado = false AND LOWER(h.estado) = 'aceptada' " +
      "GROUP BY EXTRACT(HOUR FROM h.fechaAcontecimiento) " +
      "ORDER BY COUNT(*) DESC LIMIT 1")
  Integer findHoraMasComunDeCategoria(@Param("categoria") String categoria);

  @Query("SELECT h.ubicacion.provincia FROM Hecho h " +
      "WHERE h.categoria = :categoria AND h.eliminado = false AND LOWER(h.estado) = 'aceptada' " +
      "AND h.ubicacion.provincia IS NOT NULL " +
      "GROUP BY h.ubicacion.provincia " +
      "ORDER BY COUNT(h) DESC LIMIT 1")
  String findProvinciaConMasHechosPorCategoria(@Param("categoria") String categoria);
}



