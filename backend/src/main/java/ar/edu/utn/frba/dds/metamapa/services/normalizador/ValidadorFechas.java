package ar.edu.utn.frba.dds.metamapa.services.normalizador;

import ar.edu.utn.frba.dds.metamapa.exceptions.FechaInvalidaException;

import java.time.LocalDateTime;

public class ValidadorFechas {

    private LocalDateTime fechaMinima;

    public ValidadorFechas() {
        this.fechaMinima = LocalDateTime.of(1900, 1, 1, 0, 0);
    }

    public LocalDateTime normalizar(LocalDateTime fecha) {
        if (fecha == null) {
            // No hacemos nada, dejamos null
            return null;
        }

        fecha = fecha.withNano(0);
        LocalDateTime ahora = LocalDateTime.now().withNano(0);

        if (fecha.isBefore(fechaMinima)) {
            throw new FechaInvalidaException(
                    "La fecha no puede ser anterior a " + fechaMinima
            );
        }

        if (fecha.isAfter(ahora)) {
            throw new FechaInvalidaException(
                    "La fecha no puede ser futura"
            );
        }


        return fecha;
    }
}
