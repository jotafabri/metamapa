package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.metamapa.adapters.IGeorreferenciacionAdapter;
import ar.edu.utn.frba.dds.metamapa.models.entities.Estadistica;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Ubicacion;
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
  private IHechosRepository hechosRepository;

  @Autowired
  private IGeorreferenciacionAdapter georreferenciacionAdapter;

  @Override
  public void actualizarEstadisticas() {
    this.agregacionService.refrescarColecciones();
    this.estadisticasRepository.deleteAll();
    this.actualizarDetalleHechos();
    this.generarEstadisticasConsolidadas();
  }

  private void actualizarDetalleHechos() {
    for (Hecho hecho : this.hechosRepository.findAllSinProvincia()) {
      String provincia = this.georreferenciacionAdapter.getNombreProvincia(hecho.getLatitud(), hecho.getLongitud());
      Ubicacion ubicacion = Ubicacion.builder().provincia(provincia).build();
      hecho.setUbicacion(ubicacion);
    }
  }

  @Override
  public String obtenerProvinciaConMasHechosEnColeccion(String coleccionHandle) {
    return this.hechosRepository.findProvinciaConMasHechosPorColeccion(coleccionHandle);
  }

  @Override
  public String obtenerCategoriaConMasHechos() {
    return this.hechosRepository.findCategoriaConMasHechos();
  }

  @Override
  public String obtenerProvinciaConMasHechosDeCategoria(String categoria) {
    return this.hechosRepository.findProvinciaConMasHechosPorCategoria(categoria);
  }

  @Override
  public Integer obtenerHoraConMasHechosDeCategoria(String categoria) {
    return this.hechosRepository.findHoraMasComunDeCategoria(categoria);
  }

  @Override
  public Long obtenerCantidadSolicitudesSpam() {
    return this.solicitudesRepository.countByEsSpamTrue();
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
}
