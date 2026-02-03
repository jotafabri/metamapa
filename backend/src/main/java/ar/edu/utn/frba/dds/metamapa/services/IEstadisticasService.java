package ar.edu.utn.frba.dds.metamapa.services;

import org.springframework.core.io.Resource;

public interface IEstadisticasService {
  
  void actualizarEstadisticas();
  
  String obtenerProvinciaConMasHechosEnColeccion(String coleccionHandle);
  
  String obtenerCategoriaConMasHechos();
  
  String obtenerProvinciaConMasHechosDeCategoria(String categoria);
  
  Integer obtenerHoraConMasHechosDeCategoria(String categoria);
  
  Long obtenerCantidadSolicitudesSpam();
  
  Resource exportarEstadisticasCSV();

  Integer obtenerHoraConMasHechosGlobal();

  String obtenerProvinciaConMasHechosGlobal();
}