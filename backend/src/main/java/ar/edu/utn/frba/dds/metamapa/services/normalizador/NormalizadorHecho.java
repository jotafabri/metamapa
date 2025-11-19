package ar.edu.utn.frba.dds.metamapa.services.normalizador;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;

import java.time.LocalDateTime;

public interface NormalizadorHecho {

    Hecho normalizar(Hecho hecho);

    default String normalizarCampo(String campo) {
        return campo;
    }

    default String normalizarCategoria(String categoria) {
        return categoria;
    }

    default String normalizarUbicacion(String ubicacion) {
        return ubicacion;
    }

    default LocalDateTime normalizarFecha(LocalDateTime fecha) {
        return fecha;
    }
}
