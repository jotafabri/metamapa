package ar.edu.utn.frba.dds.metamapa.schedulers;

import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import ar.edu.utn.frba.dds.metamapa.services.IAlgoritmoConsensoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MasterScheduller {

    private static final Logger log = LoggerFactory.getLogger(MasterScheduller.class);

    private final IAgregacionService agregacionService;
    private final IAlgoritmoConsensoService algoritmoConsensoService;

    public MasterScheduller(IAgregacionService agregacionService,
                           IAlgoritmoConsensoService algoritmoConsensoService) {
        this.agregacionService = agregacionService;
        this.algoritmoConsensoService = algoritmoConsensoService;
    }

    @Scheduled(cron = "0 */5 * * * *") // cada 5 minutos, por ejemplo
    public void ejecutarTodo() {
        log.info("MasterScheduler - Iniciando refresco de colecciones");
        agregacionService.refrescarColecciones();
        log.info("MasterScheduler - Refresco completado");

        log.info("MasterScheduler - Iniciando actualización de curados");
        algoritmoConsensoService.ejecutarAlgoritmos();
        log.info("MasterScheduler - Actualización de curados completada");
    }
}
