package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Persistente;
import ar.edu.utn.frba.dds.metamapa.models.entities.PersistenteIdSecuencial;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)// TODO cambiar a table per class o single table
@Table(name = "fuente")
public abstract class Fuente extends PersistenteIdSecuencial {


  /*
  @ManyToMany
  @JoinTable(
      name = "fuente_hecho",
      joinColumns = @JoinColumn(name = "fuente_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id")
  )*/

  @OneToMany(mappedBy = "fuente", orphanRemoval = true)
  protected List<Hecho> hechos = new ArrayList<>();


  public abstract List<Hecho> getHechos();

}
