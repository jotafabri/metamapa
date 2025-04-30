package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import java.util.Optional;
import java.util.stream.Stream;


@Getter
public class FuenteDinamica extends Fuente {
  public List<SolicitudEliminacion> solicitudesEliminacion = new ArrayList<>();
}
