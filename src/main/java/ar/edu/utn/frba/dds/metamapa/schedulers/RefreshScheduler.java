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

    // @Scheduled(cron = "*/10 * * * * *") // Cada 10 segundos
    // Refresca las colecciones una vez por hora
    @Scheduled(cron = "0 0 * * * *") // Cada hora en punto
    public void refrescarColecciones() {
        System.out.println("Refrescando colecciones...");
        servicioDeAgregacion.refrescarColecciones();
        System.out.println("Colecciones actualizadas.");
    }
}
