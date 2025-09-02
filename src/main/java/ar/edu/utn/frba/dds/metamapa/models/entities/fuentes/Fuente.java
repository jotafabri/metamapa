package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Fuente {
  protected List<Hecho> hechos = new ArrayList<>();
  Long id;

  public abstract List<Hecho> getHechos();

  public abstract Hecho getHechoFromId(Long id);

}
