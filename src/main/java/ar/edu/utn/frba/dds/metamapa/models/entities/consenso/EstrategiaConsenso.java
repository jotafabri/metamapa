package ar.edu.utn.frba.dds.metamapa.models.entities.consenso;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;

public interface EstrategiaConsenso {
  boolean cumple(Hecho hecho, List<Fuente> fuentes);
}
