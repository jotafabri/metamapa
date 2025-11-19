package ar.edu.utn.frba.dds.metamapa_front.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasService {
  @Autowired
  private MetamapaApiService metamapaApiService;

  public String obtenerProvinciaConMasHechosEnColeccion(String coleccionHandle) {
    return metamapaApiService.obtenerProvinciaConMasHechosEnColeccion(coleccionHandle);
  }

  public String obtenerCategoriaConMasHechos() {
    return metamapaApiService.obtenerCategoriaConMasHechos();
  }

  public String obtenerProvinciaConMasHechosDeCategoria(String categoria) {
    return metamapaApiService.obtenerProvinciaConMasHechosDeCategoria(categoria);
  }

  public Integer obtenerHoraConMasHechosDeCategoria(String categoria) {
    return metamapaApiService.obtenerHoraConMasHechosDeCategoria(categoria);
  }

  public Long obtenerCantidadSolicitudesSpam() {
    return metamapaApiService.obtenerCantidadSolicitudesSpam();
  }
}
