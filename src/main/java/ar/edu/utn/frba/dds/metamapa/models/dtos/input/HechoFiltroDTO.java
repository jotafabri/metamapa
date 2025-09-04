package ar.edu.utn.frba.dds.metamapa.models.dtos.input;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.Filtro;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroContribuyente;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroFechaAcontecimiento;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroFechaCarga;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroMultimedia;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.FiltroUbicacion;

public class HechoFiltroDTO {
  private String categoria;
  private String fechaReporteDesde;
  private String fechaReporteHasta;
  private String fechaAcontecimientoDesde;
  private String fechaAcontecimientoHasta;
  private String ubicacion;
  private Boolean soloConMultimedia;
  private Boolean soloConContribuyente;

  public List<Filtro> getList() {
    List<Filtro> criterios = new ArrayList<Filtro>();

    // Agrego criterio de categoria
    if (categoria != null && !categoria.isEmpty()) {
      criterios.add(new FiltroCategoria(categoria));
    }

    // Agrego criterio de fecha de acontecimiento
    LocalDateTime fAcontDesde = this.parseFecha(this.fechaAcontecimientoDesde);
    LocalDateTime fAcontHasta = this.parseFecha(this.fechaAcontecimientoHasta);
    if (fAcontDesde != null || fAcontHasta != null) {
      criterios.add(new FiltroFechaAcontecimiento(fAcontDesde, fAcontHasta));
    }

    // Agrego criterio de fecha de carga
    LocalDateTime fCargaDesde = this.parseFecha(this.fechaReporteDesde);
    LocalDateTime fCargaHasta = this.parseFecha(this.fechaReporteHasta);
    if (fCargaDesde != null || fCargaHasta != null) {
      criterios.add(new FiltroFechaCarga(fCargaDesde, fCargaHasta));
    }

    if (ubicacion != null) {
      criterios.add(FiltroUbicacion.fromString(ubicacion));
    }

    if (soloConMultimedia != null && soloConMultimedia) {
      criterios.add(new FiltroMultimedia(true));
    }

    if (soloConContribuyente != null && soloConContribuyente) {
      criterios.add(new FiltroContribuyente(true));
    }

    return criterios;
  }

  private LocalDateTime parseFecha(String fecha) {
    if (fecha == null || fecha.isEmpty()) {
      return null;
    }
    return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay();
  }
}
