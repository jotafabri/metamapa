package ar.edu.utn.frba.dds.metamapa.models.repositories;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IColeccionesRepository extends JpaRepository<Coleccion, Long> {
  @Query("SELECT col FROM Coleccion col ORDER BY col.fechaAlta LIMIT :limit")
  List<Coleccion> findAllLimit(@Param("limit") Integer limit);

  Coleccion findColeccionByHandle(String handle);

  boolean existsByHandle(String candidato);

  void deleteColeccionByHandle(String handle);

  @Query("SELECT DISTINCT h.categoria FROM Coleccion col " +
      "JOIN col.hechos h " +
      "WHERE col.handle = :handle")
  List<String> findDistinctCategoriasByHandle(@Param("handle") String handle);

  @Query("SELECT h.categoria, h.ubicacion.pais, h.ubicacion.provincia, h.ubicacion.localidad " +
      "FROM Coleccion col " +
      "JOIN col.hechos h " +
      "WHERE col.handle = :handle")
  List<Object[]> findDatosRawByHandle(@Param("handle") String handle);

  @Query("SELECT f.id FROM Coleccion col " +
      "JOIN col.fuentes f " +
      "WHERE col.handle = :handle")
  List<Long> findAllFuenteIdsByHandle(String handle);

  @Modifying
  @Query(value = "DELETE FROM coleccion_fuente WHERE coleccion_id = :coleccionId", nativeQuery = true)
  void deleteAllFuentesByColeccionId(@Param("coleccionId") Long coleccionId);

  @Modifying
  @Query(value = "INSERT INTO coleccion_fuente (coleccion_id, fuente_id) VALUES (:coleccionId, :fuenteId)", nativeQuery = true)
  void insertFuenteForColeccion(@Param("coleccionId") Long coleccionId, @Param("fuenteId") Long fuenteId);
}