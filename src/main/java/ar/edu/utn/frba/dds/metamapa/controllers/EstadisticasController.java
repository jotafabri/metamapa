package ar.edu.utn.frba.dds.metamapa.controllers;

import ar.edu.utn.frba.dds.metamapa.services.IEstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {

  @Autowired
  private IEstadisticasService estadisticasService;

  @GetMapping("/actualizar")
  public ResponseEntity<String> actualizarEstadisticas() {
    estadisticasService.actualizarEstadisticas();
    return ResponseEntity.ok("Estadísticas actualizadas correctamente");
  }

  @GetMapping("/provincia-mas-hechos-coleccion")
  public ResponseEntity<String> obtenerProvinciaConMasHechosEnColeccion(
      @RequestParam String coleccionHandle) {
    String provincia = estadisticasService.obtenerProvinciaConMasHechosEnColeccion(coleccionHandle);
    return ResponseEntity.ok("En la colección '" + coleccionHandle + "', la provincia con más hechos es: " + provincia);
  }

  @GetMapping("/categoria-mas-hechos")
  public ResponseEntity<String> obtenerCategoriaConMasHechos() {
    String categoria = estadisticasService.obtenerCategoriaConMasHechos();
    return ResponseEntity.ok("La categoría con más hechos reportados es: " + categoria);
  }

  @GetMapping("/provincia-mas-hechos-categoria")
  public ResponseEntity<String> obtenerProvinciaConMasHechosDeCategoria(
      @RequestParam String categoria) {
    String provincia = estadisticasService.obtenerProvinciaConMasHechosDeCategoria(categoria);
    return ResponseEntity.ok("Para la categoría '" + categoria + "', la provincia con más hechos es: " + provincia);
  }

  @GetMapping("/hora-mas-hechos-categoria")
  public ResponseEntity<String> obtenerHoraConMasHechosDeCategoria(
      @RequestParam String categoria) {
    Integer hora = estadisticasService.obtenerHoraConMasHechosDeCategoria(categoria);
    String respuesta = hora == -1 ? "Sin datos" : hora + ":00";
    return ResponseEntity.ok("Para la categoría '" + categoria + "', la hora con más hechos es: " + respuesta);
  }

  @GetMapping("/solicitudes-spam")
  public ResponseEntity<String> obtenerCantidadSolicitudesSpam() {
    Long cantidad = estadisticasService.obtenerCantidadSolicitudesSpam();
    return ResponseEntity.ok("Cantidad de solicitudes de eliminación que son spam: " + cantidad);
  }

  @GetMapping("/exportar")
  public ResponseEntity<Resource> exportarEstadisticasCSV() {
    Resource resource = estadisticasService.exportarEstadisticasCSV();
    
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estadisticas_metamapa.csv")
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(resource);
  }
}