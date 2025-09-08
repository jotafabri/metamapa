package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.Estadistica;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IEstadisticasRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import ar.edu.utn.frba.dds.metamapa.services.IDetectorSpam;
import ar.edu.utn.frba.dds.metamapa.services.IEstadisticasService;
import ar.edu.utn.frba.dds.metamapa.services.IGeocodificacionService;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasService implements IEstadisticasService {

  @Autowired
  private IEstadisticasRepository estadisticasRepository;

  @Autowired
  private IAgregacionService agregacionService;

  @Autowired
  private ISolicitudesEliminacionRepository solicitudesRepository;

  @Autowired
  private IColeccionesRepository coleccionesRepository;

  @Autowired
  private IDetectorSpam detectorSpam;

  @Autowired
  private IGeocodificacionService geocodificacionService;

  @Override
  public void actualizarEstadisticas() {
    agregacionService.refrescarColecciones();
    estadisticasRepository.deleteAll();
    generarEstadisticasConsolidadas();
  }

  @Override
  public String obtenerProvinciaConMasHechosEnColeccion(String coleccionHandle) {
    Coleccion coleccion = coleccionesRepository.findByHandle(coleccionHandle);
    
    Map<String, Long> hechosPorProvincia = coleccion.getHechos()
        .stream()
        .filter(h -> h.getEstado() == Estado.ACEPTADA && !h.getEliminado())
        .collect(Collectors.groupingBy(
            h -> geocodificacionService.obtenerProvincia(h.getLatitud(), h.getLongitud()),
            Collectors.counting()
        ));

    return hechosPorProvincia.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("Sin datos");
  }

  @Override
  public String obtenerCategoriaConMasHechos() {
    Map<String, Long> hechosPorCategoria = obtenerTodosLosHechosAceptados()
        .stream()
        .collect(Collectors.groupingBy(Hecho::getCategoria, Collectors.counting()));

    return hechosPorCategoria.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("Sin datos");
  }

  @Override
  public String obtenerProvinciaConMasHechosDeCategoria(String categoria) {
    Map<String, Long> hechosPorProvincia = obtenerTodosLosHechosAceptados()
        .stream()
        .filter(h -> categoria.equals(h.getCategoria()))
        .collect(Collectors.groupingBy(
            h -> geocodificacionService.obtenerProvincia(h.getLatitud(), h.getLongitud()),
            Collectors.counting()
        ));

    return hechosPorProvincia.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("Sin datos");
  }

  @Override
  public Integer obtenerHoraConMasHechosDeCategoria(String categoria) {
    Map<Integer, Long> hechosPorHora = obtenerTodosLosHechosAceptados()
        .stream()
        .filter(h -> categoria.equals(h.getCategoria()) && h.getFechaAcontecimiento() != null)
        .collect(Collectors.groupingBy(
            h -> h.getFechaAcontecimiento().getHour(),
            Collectors.counting()
        ));

    return hechosPorHora.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(-1);
  }

  @Override
  public Long obtenerCantidadSolicitudesSpam() {
    return solicitudesRepository.findAll()
        .stream()
        .filter(solicitud -> detectorSpam.esSpam(solicitud.getCausa()))
        .count();
  }

  @Override
  public Resource exportarEstadisticasCSV() {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      CSVWriter writer = new CSVWriter(new OutputStreamWriter(out));

      String[] header = {"Pregunta", "Parametro", "Respuesta", "Fecha"};
      writer.writeNext(header);

      LocalDateTime ahora = LocalDateTime.now();

      for (Estadistica estadistica : estadisticasRepository.findAll()) {
        String[] data = {
            estadistica.getPregunta(),
            estadistica.getParametro() != null ? estadistica.getParametro() : "",
            estadistica.getRespuesta(),
            estadistica.getFechaGeneracion().toString()
        };
        writer.writeNext(data);
      }

      writer.close();
      byte[] bytes = out.toByteArray();
      return new ByteArrayResource(bytes);
    } catch (IOException e) {
      throw new RuntimeException("Error generando CSV de estadísticas", e);
    }
  }

  private void generarEstadisticasConsolidadas() {
    String categoriaTop = obtenerCategoriaConMasHechos();
    Estadistica estadCategoriaTop = Estadistica.builder()
        .pregunta("Categoria con mas hechos")
        .parametro("")
        .respuesta(categoriaTop)
        .build();
    estadisticasRepository.save(estadCategoriaTop);

    Long solicitudesSpam = obtenerCantidadSolicitudesSpam();
    Estadistica estadSpam = Estadistica.builder()
        .pregunta("Solicitudes spam")
        .parametro("")
        .respuesta(solicitudesSpam.toString())
        .build();
    estadisticasRepository.save(estadSpam);
  }

  private Coleccion obtenerColeccionPorHandle(String coleccionHandle) {
    return agregacionService.obtenerColecciones()
        .stream()
        .filter(c -> coleccionHandle.equals(c.getHandle()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Colección no encontrada: " + coleccionHandle));
  }

  private List<Hecho> obtenerTodosLosHechosAceptados() {
    return agregacionService.obtenerColecciones()
        .stream()
        .flatMap(c -> c.getHechos().stream())
        .filter(h -> h.getEstado() == Estado.ACEPTADA && !h.getEliminado())
        .toList();
  }
}