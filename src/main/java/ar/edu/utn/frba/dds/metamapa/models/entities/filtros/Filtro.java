package ar.edu.utn.frba.dds.metamapa.models.entities.filtros;

import ar.edu.utn.frba.dds.metamapa.models.entities.Persistente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.*;

/*
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // TODO cambiarlo a single_table
@Table(name = "criterio")
*/
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_filtro", discriminatorType = DiscriminatorType.STRING)
@Table(name = "criterio")

public abstract class Filtro extends Persistente {
  public abstract Boolean cumple(Hecho hecho);
}
