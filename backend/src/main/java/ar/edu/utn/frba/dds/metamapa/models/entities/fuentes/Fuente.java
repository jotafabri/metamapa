package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Persistente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)// TODO cambiar a table per class o single table
@Table(name = "fuente")
public abstract class Fuente extends Persistente {

  @ManyToMany
  @JoinTable(
      name = "fuente_hecho",
      joinColumns = @JoinColumn(name = "fuente_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id")
  )
  // TODO no es many to many
  protected List<Hecho> hechos = new ArrayList<>();

  public abstract List<Hecho> getHechos();

}
