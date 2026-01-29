package ar.edu.utn.frba.dds.metamapa.schedulers;

import ar.edu.utn.frba.dds.metamapa.services.IAlgoritmoConsensoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AlgorithmScheduler {

  private static final Logger log = LoggerFactory.getLogger(AlgorithmScheduler.class);

  private final IAlgoritmoConsensoService algoritmoConsensoService;

  public AlgorithmScheduler(IAlgoritmoConsensoService algoritmoConsensoService) {
    this.algoritmoConsensoService = algoritmoConsensoService;
  }

  // Ejecuta cada 30 segundos pero desfasado 15s para no coincidir
  //@Scheduled(cron = "15/30 * * * * *")
  public void ejecutarAlgoritmos() {
    log.info("Scheduler2 - Ejecutando Algoritmo de Consenso.");
    this.algoritmoConsensoService.ejecutarAlgoritmos();
    log.info("Scheduler2 - Ejecucion de algoritmo de consenso completada.");
  }
}

