package ar.edu.utn.frba.dds.metamapa.models.entities.fuentes;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Contribuyente;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fuente_dinamica")
public class FuenteDinamica extends Fuente {
  @ManyToOne(fetch = FetchType.EAGER)
  private Contribuyente contribuyente;

  public List<Hecho> getHechos() {
    return (hechos.stream().filter(h -> h.getEliminado().equals(false)).toList());
  }

  public Hecho getHechoFromId(Long id) {
    return hechos.stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
  }

  public Hecho buscarHechoPorTitulo(String titulo) {
    return getHechos().stream().filter(h -> h.getTitulo().equals(titulo)).findFirst().orElse(null);
  }

  public void etiquetarPorTitulo(String titulo, String etiqueta) {
    buscarHechoPorTitulo(titulo).agregarEtiqueta(etiqueta);
  }

  public void agregarHecho(Hecho hecho) {
    if (!this.hechos.contains(hecho)) {
      this.hechos.add(hecho);
    }
  }

  public void agregarHechos(List<Hecho> hechos) {
    hechos.forEach(this::agregarHecho);
  }
}