package ar.edu.utn.frba.dds.metamapa.services.normalizador;

import java.time.LocalDateTime;

public class ValidadorFechas {

    private LocalDateTime fechaMinima;
    private LocalDateTime fechaMaxima;

    public ValidadorFechas() {
        this.fechaMinima = LocalDateTime.of(1900, 1, 1, 0, 0);
        this.fechaMaxima = LocalDateTime.now().plusYears(1); // por ejemplo
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

        // Asegurar que la fecha esté dentro del rango permitido
        if (fecha.isBefore(fechaMinima)) {
            return fechaMinima;
        }

        if (fecha.isAfter(fechaMaxima)) {
            return fechaMaxima;
        }

        // Opcional: redondear a segundos para uniformidad
        return fecha.withNano(0);
    }
}
