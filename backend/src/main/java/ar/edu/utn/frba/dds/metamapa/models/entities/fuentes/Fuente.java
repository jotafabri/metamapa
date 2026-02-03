package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.Persistente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)// TODO cambiar a table per class o single table
@Table(name = "fuente")
@DiscriminatorColumn(name = "tipo")
public abstract class Fuente extends Persistente {
  @Column(name = "ruta")
  protected String ruta;

  @Column(name = "nombre")
  protected String nombre;

  @Column(name = "titulo")
  protected String titulo;

  @OneToMany(mappedBy = "fuente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  protected List<Hecho> hechos = new ArrayList<>();

  public abstract List<Hecho> getHechos();

}
