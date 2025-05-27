package ar.edu.utn.frba.dds.metamapa.models.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Fuente {
  Long id;

  List<Hecho> getHechos() {
    return null;
  }

}
