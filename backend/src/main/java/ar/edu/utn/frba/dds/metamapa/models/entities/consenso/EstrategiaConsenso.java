package ar.edu.utn.frba.dds.metamapa.models.entities.consenso;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;

public interface EstrategiaConsenso {
  boolean cumple(Hecho hecho, List<Fuente> fuentes);
  List<Hecho> filtrarConsensuados(List<Hecho> hechos, List<Fuente> fuentes);
  String getNombre();
}
