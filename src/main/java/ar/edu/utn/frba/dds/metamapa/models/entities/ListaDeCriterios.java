package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ListaDeCriterios {
  private List<CriterioPertenencia> criterios;

  public ListaDeCriterios() {
    this.criterios = new ArrayList<CriterioPertenencia>();
  }

  public List<CriterioPertenencia> getListFromParams(String categoria,
                                                     String fecha_reporte_desde,
                                                     String fecha_reporte_hasta,
                                                     String fecha_acontecimiento_desde,
                                                     String fecha_acontecimiento_hasta,
                                                     String ubicacion) {
    // Agrego criterio de categoria
    if (categoria != null && !categoria.isEmpty()) {
      this.criterios.add(new CriterioCategoria(categoria));
    }

    // Agrego criterio de fecha de acontecimiento
    var fAcontDesde = this.parseFecha(fecha_acontecimiento_desde);
    var fAcontHasta = this.parseFecha(fecha_acontecimiento_hasta);
    if (fAcontDesde != null || fAcontHasta != null) {
      this.criterios.add(new CriterioFechaAcontecimiento(fAcontDesde, fAcontHasta));
    }

    // Agrego criterio de fecha de carga
    var fCargaDesde = this.parseFecha(fecha_reporte_desde);
    var fCargaHasta = this.parseFecha(fecha_reporte_hasta);
    if (fCargaDesde != null || fCargaHasta != null) {
      this.criterios.add(new CriterioFechaCarga(fCargaDesde, fCargaHasta));
    }

    if (ubicacion != null) {
      this.criterios.add(CriterioUbicacion.fromString(ubicacion));
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
