package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Persistente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "fuente")
public abstract class Fuente extends Persistente {

  @ManyToMany
  @JoinTable(
      name = "fuente_hecho",
      joinColumns = @JoinColumn(name = "fuente_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id")
  )
  protected List<Hecho> hechos = new ArrayList<>();

  public abstract List<Hecho> getHechos();

}
