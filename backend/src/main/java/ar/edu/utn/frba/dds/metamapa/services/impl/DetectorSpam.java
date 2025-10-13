package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.services.IDetectorSpam;
import org.springframework.stereotype.Component;

@Component
public class DetectorSpam implements IDetectorSpam {

  @Override
  public boolean esSpam(String texto) {
    return texto.length() > 50000;
  }
}
