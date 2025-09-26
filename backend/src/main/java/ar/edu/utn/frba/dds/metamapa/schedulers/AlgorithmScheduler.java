package ar.edu.utn.frba.dds.metamapa.schedulers;

import ar.edu.utn.frba.dds.metamapa.services.IAlgoritmoConsensoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlgorithmScheduler {
  private final IAlgoritmoConsensoService algoritmoConsensoService;

  public AlgorithmScheduler(IAlgoritmoConsensoService algoritmoConsensoService) {
    this.algoritmoConsensoService = algoritmoConsensoService;
  }

  @Scheduled(cron = "${tiempo.algoritmo}")
  public void ejecutarAlgoritmos() {
    this.algoritmoConsensoService.ejecutarAlgoritmos();
  }
}

