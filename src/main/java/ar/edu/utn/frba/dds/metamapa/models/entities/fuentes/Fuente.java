package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Fuente {
  Long id;

  public List<Hecho> getHechos() {
    return null;
  }

  public Hecho getHechoFromId(Long id) {
    return null;
  }

}
