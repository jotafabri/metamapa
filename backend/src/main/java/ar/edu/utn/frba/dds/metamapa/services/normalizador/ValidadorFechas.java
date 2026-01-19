package ar.edu.utn.frba.dds.metamapa.services.normalizador;

import ar.edu.utn.frba.dds.metamapa.exceptions.FechaInvalidaException;

import java.time.LocalDateTime;

public class ValidadorFechas {

    private LocalDateTime fechaMinima;
    private LocalDateTime fechaMaxima;

    public ValidadorFechas() {
        this.fechaMinima = LocalDateTime.of(1900, 1, 1, 0, 0);
        this.fechaMaxima = LocalDateTime.now();
    }

    public ValidadorFechas(LocalDateTime fechaMinima, LocalDateTime fechaMaxima) {
        this.fechaMinima = fechaMinima;
        this.fechaMaxima = fechaMaxima;
    }

    public LocalDateTime normalizar(LocalDateTime fecha) {
        if (fecha == null) {
            // No hacemos nada, dejamos null
            return null;
        }

        if (fecha.isBefore(fechaMinima)) {
            throw new FechaInvalidaException(
                    "La fecha no puede ser anterior a " + fechaMinima
            );
        }

        if (fecha.isAfter(fechaMaxima)) {
            throw new FechaInvalidaException(
                    "La fecha no puede ser futura"
            );
        }

        // Opcional: redondear a segundos para uniformidad
        return fecha.withNano(0);
    }
}
