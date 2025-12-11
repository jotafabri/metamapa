package ar.edu.utn.frba.dds.metamapa.schedulers;

import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshScheduler {

  private final IAgregacionService servicioDeAgregacion;

  public RefreshScheduler(IAgregacionService servicioDeAgregacion) {
    this.servicioDeAgregacion = servicioDeAgregacion;
  }

  @Scheduled(cron = "${tiempo.refresco:0 0 * * * *}") // Con esto cada hora en punto
  public void refrescarColecciones() {
    servicioDeAgregacion.refrescarColecciones();
  }
}