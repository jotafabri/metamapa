package ar.edu.utn.frba.dds.metamapa.schedulers;

import ar.edu.utn.frba.dds.metamapa.services.IEstadisticasService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EstadisticasScheduler {
  private final IEstadisticasService estadisticasService;

  public EstadisticasScheduler(IEstadisticasService estadisticasService) {
    this.estadisticasService = estadisticasService;
  }

  @Scheduled(cron = "${tiempo.estadisticas:0 0 2 * * ?}")
  public void actualizarEstadisticasPeriodicamente() {
    this.estadisticasService.actualizarEstadisticas();
  }
}