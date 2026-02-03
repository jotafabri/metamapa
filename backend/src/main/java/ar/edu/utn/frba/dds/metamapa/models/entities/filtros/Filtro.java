package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import ar.edu.utn.frba.dds.metamapa.models.entities.Persistente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // TODO cambiarlo a single_table
@Table(name = "criterio")
@DiscriminatorColumn(name = "tipo")
public abstract class Filtro extends Persistente {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coleccion_id")
  @Setter
  private Coleccion coleccion;

  public abstract Boolean cumple(Hecho hecho);
}
