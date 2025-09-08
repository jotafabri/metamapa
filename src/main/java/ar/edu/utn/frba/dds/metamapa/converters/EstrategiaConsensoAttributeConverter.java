package ar.edu.utn.frba.dds.metamapa.converters;

import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.ConsensoAbsoluto;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.ConsensoPorMayoriaSimple;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.ConsensoPorMultiplesMenciones;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.ConsensoTrue;
import ar.edu.utn.frba.dds.metamapa.models.entities.consenso.EstrategiaConsenso;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstrategiaConsensoAttributeConverter implements AttributeConverter<EstrategiaConsenso, String> {

  @Override
  public String convertToDatabaseColumn(EstrategiaConsenso estrategiaConsenso) {
    if (estrategiaConsenso == null) {
      return null;
    }

    String estrategia = "";

    if (estrategiaConsenso instanceof ConsensoTrue) {
      estrategia = "DEFAULT";
    } else if (estrategiaConsenso instanceof ConsensoAbsoluto) {
      estrategia = "ABSOLUTO";
    } else if (estrategiaConsenso instanceof ConsensoPorMayoriaSimple) {
      estrategia = "MAYORIA_SIMPLE";
    } else if (estrategiaConsenso instanceof ConsensoPorMultiplesMenciones) {
      estrategia = "MULTIPLES_MENCIONES";
    }
    return estrategia;
  }

  @Override
  public EstrategiaConsenso convertToEntityAttribute(String s) {
    if (s == null) {
      return null;
    }
    return switch (s) {
      case "DEFAULT" -> new ConsensoTrue();
      case "ABSOLUTO" -> new ConsensoAbsoluto();
      case "MAYORIA_SIMPLE" -> new ConsensoPorMayoriaSimple();
      case "MULTIPLES_MENCIONES" -> new ConsensoPorMultiplesMenciones();
      default -> null;
    };
  }
}
