package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.metamapa.adapters.IGeorreferenciacionAdapter;
import ar.edu.utn.frba.dds.metamapa.models.entities.Estadistica;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Estado;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Ubicacion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IColeccionesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IEstadisticasRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.ISolicitudesEliminacionRepository;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import ar.edu.utn.frba.dds.metamapa.services.IDetectorSpam;
import ar.edu.utn.frba.dds.metamapa.services.IEstadisticasService;
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
  private IHechosRepository hechosRepository;

  @Autowired
  private IDetectorSpam detectorSpam;

  @Autowired
  private IGeorreferenciacionAdapter georreferenciacionAdapter;

  @Override
  public void actualizarEstadisticas() {
    this.agregacionService.refrescarColecciones();
    this.estadisticasRepository.deleteAll();
    this.actualizarDetalleHechos();// TODO generar detalle de ubicacion para todos los hechos con api externa
    this.generarEstadisticasConsolidadas();
  }

  private void actualizarDetalleHechos() {
      for (Hecho hecho : this.hechosRepository.findAllByEliminadoFalse()) {
          String provincia = this.georreferenciacionAdapter.getNombreProvincia(hecho.getLatitud(), hecho.getLongitud());
          Ubicacion ubicacion = Ubicacion.builder().provincia(provincia).build();
          hecho.setUbicacion(ubicacion);
      }
  }

  @Override
  public String obtenerProvinciaConMasHechosEnColeccion(String coleccionHandle) {
    Coleccion coleccion = this.coleccionesRepository.findColeccionByHandle(coleccionHandle);
    
    Map<String, Long> hechosPorProvincia = coleccion.getHechos()
        .stream()
        .filter(h -> h.getEstado() == Estado.ACEPTADA)
        .collect(Collectors.groupingBy(
            h -> georreferenciacionAdapter.getNombreProvincia(h.getLatitud(), h.getLongitud()),
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
            h -> georreferenciacionAdapter.getNombreProvincia(h.getLatitud(), h.getLongitud()),
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

  private List<Hecho> obtenerTodosLosHechosAceptados() {
    return agregacionService.obtenerColecciones()
        .stream()
        .flatMap(c -> c.getHechos().stream())
        .filter(h -> h.getEstado() == Estado.ACEPTADA && !h.getEliminado())
        .toList();
  }
}