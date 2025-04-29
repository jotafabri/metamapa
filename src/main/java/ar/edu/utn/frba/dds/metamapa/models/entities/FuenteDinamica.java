package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

@Getter
public class FuenteDinamica extends Fuente {
  public List<SolicitudEliminacion> solicitudesEliminacion = new ArrayList<>();

  public void agregarHecho(Hecho hecho) {
    for (Hecho hecho2 : listaHechos) { // find
      if (hecho2.getTitulo().equalsIgnoreCase(hecho.getTitulo())) {
        hecho2.setDescripcion(hecho.getDescripcion());
        hecho2.setCategoria(hecho.getCategoria());
        hecho2.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
        hecho2.setFechaCarga(hecho.getFechaCarga());
        hecho2.setOrigen(hecho.getOrigen());
        return;
      }
    }
    listaHechos.add(hecho);
  }
}
