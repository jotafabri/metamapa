package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Coordenadas;
import ar.edu.utn.frba.dds.metamapa.models.entities.CriterioCategoria;
import ar.edu.utn.frba.dds.metamapa.models.entities.CriterioFechaAcontecimiento;
import ar.edu.utn.frba.dds.metamapa.models.entities.CriterioFechaCarga;
import ar.edu.utn.frba.dds.metamapa.models.entities.CriterioPertenencia;
import ar.edu.utn.frba.dds.metamapa.models.entities.CriterioUbicacion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.services.IHechosService;
import org.springframework.stereotype.Service;

@Service
public class HechosService implements IHechosService {
  private IHechosRepository hechosRepository;
  private IColeccionesRepository coleccionesRepository;

  @Override
  public List<HechoDTO> getHechosWithParams(String categoria,
                                            String fecha_reporte_desde,
                                            String fecha_reporte_hasta,
                                            String fecha_acontecimiento_desde,
                                            String fecha_acontecimiento_hasta,
                                            String ubicacion) {
    var filtro = new ArrayList<CriterioPertenencia>();

    // Agrego criterio de categoria
    if (categoria != null && !categoria.isEmpty()) {
      filtro.add(new CriterioCategoria(categoria));
    }

    // Agrego criterio de fecha de acontecimiento
    var fAcontDesde = this.parseFecha(fecha_acontecimiento_desde);
    var fAcontHasta = this.parseFecha(fecha_acontecimiento_hasta);
    if (fAcontDesde != null || fAcontHasta != null) {
      filtro.add(new CriterioFechaAcontecimiento(fAcontDesde, fAcontHasta));
    }

    // Agrego criterio de fecha de carga
    var fCargaDesde = this.parseFecha(fecha_reporte_desde);
    var fCargaHasta = this.parseFecha(fecha_reporte_hasta);
    if (fCargaDesde != null || fCargaHasta != null) {
      filtro.add(new CriterioFechaCarga(fCargaDesde, fCargaHasta));
    }

    var coordenadas = Coordenadas.fromString(ubicacion);
    if(coordenadas != null) {
      filtro.add(new CriterioUbicacion(coordenadas));
    }

    return this.hechosRepository.findAll()
        .stream()
        .filter(h -> filtro.stream().allMatch(c -> c.cumple(h)))
        .map(HechoDTO::fromHecho)
        .toList();
  }

  private LocalDateTime parseFecha(String fecha) {
    if (fecha == null || fecha.isEmpty()) {
      return null;
    }
    return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
  }
}
