package ar.edu.utn.frba.dds.metamapa.schedulers;

import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshScheduler {

  private static final Logger log = LoggerFactory.getLogger(RefreshScheduler.class);

  private final IAgregacionService servicioDeAgregacion;

  public RefreshScheduler(IAgregacionService servicioDeAgregacion) {
    this.servicioDeAgregacion = servicioDeAgregacion;
  }

  // Se ejecuta cada 1 minuto
  //@Scheduled(cron = "0 * * * * *")
  public void refrescarColecciones() {
    log.info("Scheduler1 - Refrescando colecciones automáticamente...");
    servicioDeAgregacion.refrescarColecciones();
    log.info("Scheduler1 - Refresco de colecciones completado.");
  }
}
