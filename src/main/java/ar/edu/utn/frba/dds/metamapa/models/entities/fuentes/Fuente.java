package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class Fuente {
  Long id;

  public List<Hecho> getHechos() {
    return null;
  }

}
