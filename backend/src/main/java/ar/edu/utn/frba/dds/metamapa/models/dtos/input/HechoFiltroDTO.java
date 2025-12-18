package ar.edu.utn.frba.dds.metamapa.models.dtos.input;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.Filtro;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl.FiltroCategoria;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl.FiltroContribuyente;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl.FiltroFechaAcontecimiento;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl.FiltroFechaCarga;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl.FiltroMultimedia;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl.FiltroTitulo;
import ar.edu.utn.frba.dds.metamapa.models.entities.filtros.impl.FiltroUbicacion;
import lombok.Data;

@Data
public class HechoFiltroDTO {
  private String titulo;
  private String categoria;
  private String fechaReporteDesde;
  private String fechaReporteHasta;
  private String fechaAcontecimientoDesde;
  private String fechaAcontecimientoHasta;
  private String pais;
  private String provincia;
  private String localidad;
  private Boolean soloConMultimedia;
  private Boolean soloConContribuyente;
  private Boolean curado = false;
  private Integer page = 0;
  private Integer size = 10;

  public static HechoFiltroDTO fromCriterios(List<Filtro> criterios) {
    var dto = new HechoFiltroDTO();
    for (Filtro filtro : criterios) {
      if (filtro instanceof FiltroCategoria) {
        dto.setCategoria(((FiltroCategoria) filtro).getStringBuscado());
      } else if (filtro instanceof FiltroTitulo) {
        dto.setTitulo(((FiltroTitulo) filtro).getStringBuscado());
      } else if (filtro instanceof FiltroFechaAcontecimiento ffa) {
        if (ffa.getDesde() != null) {
          dto.setFechaAcontecimientoDesde(ffa.getDesde().toLocalDate().toString());
        }
        if (ffa.getHasta() != null) {
          dto.setFechaAcontecimientoHasta(ffa.getHasta().toLocalDate().toString());
        }
      } else if (filtro instanceof FiltroFechaCarga ffc) {
        if (ffc.getDesde() != null) {
          dto.setFechaReporteDesde(ffc.getDesde().toLocalDate().toString());
        }
        if (ffc.getHasta() != null) {
          dto.setFechaReporteHasta(ffc.getHasta().toLocalDate().toString());
        }
      } else if (filtro instanceof FiltroUbicacion fu) {
        dto.setPais(fu.getPais());
        dto.setProvincia(fu.getProvincia());
        dto.setLocalidad(fu.getLocalidad());
      } else if (filtro instanceof FiltroMultimedia) {
        dto.setSoloConMultimedia(((FiltroMultimedia) filtro).getCondicion());
      } else if (filtro instanceof FiltroContribuyente) {
        dto.setSoloConContribuyente(((FiltroContribuyente) filtro).getCondicion());
      }
    }
    return dto;
  }

  public List<Filtro> getList() {
    List<Filtro> criterios = new ArrayList<Filtro>();

    // Agrego criterio de categoria
    if (categoria != null && !categoria.isEmpty()) {
      criterios.add(new FiltroCategoria(categoria));
    }

    // Agrego criterio de titulo
    if (titulo != null && !titulo.isEmpty()) {
      criterios.add(new FiltroTitulo(titulo));
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

    if ((pais != null && !pais.isEmpty())
        || (provincia != null && !provincia.isEmpty())
        || (localidad != null && !localidad.isEmpty())) {
      criterios.add(new FiltroUbicacion(pais, provincia, localidad));
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
    return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
  }
}
