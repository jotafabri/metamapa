package ar.edu.utn.frba.dds.metamapa.services;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;

import java.time.LocalDateTime;

public interface NormalizadorHecho {

    Hecho normalizar(Hecho hecho);

    default String normalizarTitulo(String titulo) {
        return titulo;
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
