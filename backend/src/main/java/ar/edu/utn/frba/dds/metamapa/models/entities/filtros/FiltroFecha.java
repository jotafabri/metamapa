package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import java.time.LocalDateTime;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Getter
public abstract class FiltroFecha extends Filtro {
  @Column(name = "fecha_desde")
  private LocalDateTime desde;

  @Column(name = "fecha_hasta")
  private LocalDateTime hasta;

  @Override
  public Boolean cumple(Hecho hecho) {
    LocalDateTime fecha = this.getFecha(hecho);
    return (desde == null || !fecha.isBefore(desde))
        && (hasta == null || !fecha.isAfter(hasta));
  }

  protected abstract LocalDateTime getFecha(Hecho hecho);
}
