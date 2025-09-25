package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class FiltroFecha extends Filtro {
  @Column(name = "fecha_desde")
  private LocalDateTime desde;

  @Column(name = "fecha_hasta")
  private LocalDateTime hasta;

  @Override
  public Boolean cumple(Hecho hecho) {
    LocalDateTime fecha = this.getFecha(hecho);
    return (fecha.isEqual(desde) || fecha.isAfter(desde))
        && (fecha.isEqual(hasta) || fecha.isBefore(hasta));
  }

  protected abstract LocalDateTime getFecha(Hecho hecho);
}
