package ar.edu.utn.frba.dds.metamapa.models.entities.enums;

import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.ConsensoAbsoluto;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.ConsensoPorMayoriaSimple;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.ConsensoPorMultiplesMenciones;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.EstrategiaConsenso;

public enum TipoAlgoritmo {
  MAYORIA_SIMPLE {
    public EstrategiaConsenso getConsenso() {
      return new ConsensoPorMayoriaSimple();
    }
  },
  MAYORIA_ABSOLUTA {
    public EstrategiaConsenso getConsenso() {
      return new ConsensoAbsoluto();
    }
  },
  MULTIPLES_MENCIONES {
    public EstrategiaConsenso getConsenso() {
      return new ConsensoPorMultiplesMenciones();
    }
  };

  public abstract EstrategiaConsenso getConsenso();
}
