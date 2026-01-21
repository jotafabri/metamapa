package ar.edu.utn.frba.dds.metamapa.controllers;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.EstadisticasDTO;
import ar.edu.utn.frba.dds.metamapa.services.IEstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/admin/estadisticas")
@PreAuthorize("hasRole('ADMIN')")
public class EstadisticasController {

  @Autowired
  private IEstadisticasService estadisticasService;

  @GetMapping("/actualizar")
  public ResponseEntity<Object> actualizarEstadisticas() {
    estadisticasService.actualizarEstadisticas();
    return ResponseEntity.ok(java.util.Map.of("mensaje", "Estadísticas actualizadas correctamente"));
  }




  @GetMapping("/dashboard")
  public ResponseEntity<EstadisticasDTO> obtenerEstadisticasDashboard() {

    EstadisticasDTO dto = new EstadisticasDTO();

    dto.setCategoriaMasHechos(
            estadisticasService.obtenerCategoriaConMasHechos()
    );

    dto.setSolicitudesSpam(
            estadisticasService.obtenerCantidadSolicitudesSpam()
    );

    dto.setProvinciaMasHechos(
            estadisticasService.obtenerProvinciaConMasHechosGlobal()
    );

    dto.setHoraMasHechos(
            estadisticasService.obtenerHoraConMasHechosGlobal()
    );



    return ResponseEntity.ok(dto);
  }












  /*
  @GetMapping("/provincia-mas-hechos-coleccion")
  public ResponseEntity<Object> obtenerProvinciaConMasHechosEnColeccion(
      @RequestParam String coleccionHandle) {
    String provincia = estadisticasService.obtenerProvinciaConMasHechosEnColeccion(coleccionHandle);
    return ResponseEntity.ok(java.util.Map.of(
        "coleccionHandle", coleccionHandle,
        "provincia", provincia != null ? provincia : "Sin datos"
    ));
  }

  @GetMapping("/categoria-mas-hechos")
  public ResponseEntity<Object> obtenerCategoriaConMasHechos() {
    String categoria = estadisticasService.obtenerCategoriaConMasHechos();
    return ResponseEntity.ok(java.util.Map.of("categoria", categoria != null ? categoria : "Sin datos"));
  }

  @GetMapping("/provincia-mas-hechos-categoria")
  public ResponseEntity<Object> obtenerProvinciaConMasHechosDeCategoria(
      @RequestParam String categoria) {
    String provincia = estadisticasService.obtenerProvinciaConMasHechosDeCategoria(categoria);
    return ResponseEntity.ok(java.util.Map.of(
        "categoria", categoria,
        "provincia", provincia != null ? provincia : "Sin datos"
    ));
  }

  @GetMapping("/hora-mas-hechos-categoria")
  public ResponseEntity<Object> obtenerHoraConMasHechosDeCategoria(
      @RequestParam String categoria) {
    Integer hora = estadisticasService.obtenerHoraConMasHechosDeCategoria(categoria);
    return ResponseEntity.ok(java.util.Map.of(
        "categoria", categoria,
        "hora", hora
    ));
  }

  @GetMapping("/solicitudes-spam")
  public ResponseEntity<Object> obtenerCantidadSolicitudesSpam() {
    Long cantidad = estadisticasService.obtenerCantidadSolicitudesSpam();
    return ResponseEntity.ok(java.util.Map.of("cantidad", cantidad));
  }

  @GetMapping("/exportar")
  public ResponseEntity<Resource> exportarEstadisticasCSV() {
    Resource resource = estadisticasService.exportarEstadisticasCSV();
    
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estadisticas_metamapa.csv")
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(resource);
  }*/
}