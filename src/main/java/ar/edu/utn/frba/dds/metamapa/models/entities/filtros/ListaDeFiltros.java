package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ListaDeFiltros {
  private List<Filtro> criterios;

  public ListaDeFiltros() {
    this.criterios = new ArrayList<Filtro>();
  }

  public List<Filtro> getListFromParams(String categoria,
                                        String fecha_reporte_desde,
                                        String fecha_reporte_hasta,
                                        String fecha_acontecimiento_desde,
                                        String fecha_acontecimiento_hasta,
                                        String ubicacion,
                                        Boolean soloConMultimedia,
                                        Boolean soloConContribuyente) {
    // Agrego criterio de categoria
    if (categoria != null && !categoria.isEmpty()) {
      this.criterios.add(new FiltroCategoria(categoria));
    }

    // Agrego criterio de fecha de acontecimiento
    var fAcontDesde = this.parseFecha(fecha_acontecimiento_desde);
    var fAcontHasta = this.parseFecha(fecha_acontecimiento_hasta);
    if (fAcontDesde != null || fAcontHasta != null) {
      this.criterios.add(new FiltroFechaAcontecimiento(fAcontDesde, fAcontHasta));
    }

    // Agrego criterio de fecha de carga
    var fCargaDesde = this.parseFecha(fecha_reporte_desde);
    var fCargaHasta = this.parseFecha(fecha_reporte_hasta);
    if (fCargaDesde != null || fCargaHasta != null) {
      this.criterios.add(new FiltroFechaCarga(fCargaDesde, fCargaHasta));
    }

    if (ubicacion != null) {
      this.criterios.add(FiltroUbicacion.fromString(ubicacion));
    }

    if (soloConMultimedia != null && soloConMultimedia) {
      this.criterios.add(new FiltroMultimedia(true));
    }

    if (soloConContribuyente != null && soloConContribuyente) {
      this.criterios.add(new FiltroContribuyente(true));
    }

    return this.criterios;
  }

  private LocalDateTime parseFecha(String fecha) {
    if (fecha == null || fecha.isEmpty()) {
      return null;
    }
    return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay();
  }
}
