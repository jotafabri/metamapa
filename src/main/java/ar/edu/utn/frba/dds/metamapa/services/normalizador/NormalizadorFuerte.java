package ar.edu.utn.frba.dds.metamapa.services.normalizador;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Ubicacion;

import java.time.LocalDateTime;

public class NormalizadorFuerte implements NormalizadorHecho {

    private NormalizadorLigero normalizadorLigero; // para campos que solo requieren normalización ligera
    private MapeadorTexto mapeadorCategorias;
    private MapeadorTexto mapeadorUbicaciones;
    private ValidadorFechas validadorFechas;

    public NormalizadorFuerte(NormalizadorLigero normalizadorLigero,
                              MapeadorTexto mapeadorCategorias,
                              MapeadorTexto mapeadorUbicaciones,
                              ValidadorFechas validadorFechas) {
        this.normalizadorLigero = normalizadorLigero;
        this.mapeadorCategorias = mapeadorCategorias;
        this.mapeadorUbicaciones = mapeadorUbicaciones;
        this.validadorFechas = validadorFechas;
    }

    @Override
    public Hecho normalizar(Hecho hecho) {
        // 1. Normalización ligera para campos que no requieren reglas fuertes
        hecho.setTitulo(normalizadorLigero.normalizarCampo(hecho.getTitulo()));
        hecho.setDescripcion(normalizadorLigero.normalizarCampo(hecho.getDescripcion()));

        // 2. Normalización fuerte para campos específicos usando diccionarios o validaciones
        hecho.setCategoria(mapeadorCategorias.normalizar(hecho.getCategoria()));

        // 3. Normalización de la ubicación (solo los campos presentes)
        Ubicacion u = hecho.getUbicacion();
        if (u != null) {
            if (u.getLocalidad() != null) {
                u.setLocalidad(mapeadorUbicaciones.normalizar(u.getLocalidad()));
            }
            if (u.getProvincia() != null) {
                u.setProvincia(mapeadorUbicaciones.normalizar(u.getProvincia()));
            }
            if (u.getPais() != null) {
                u.setPais(mapeadorUbicaciones.normalizar(u.getPais()));
            }
            hecho.setUbicacion(u);
        }

        // 3.1 Validación de coordenadas
        if (hecho.getLatitud() != null) {
            if (hecho.getLatitud() < -90 || hecho.getLatitud() > 90) {
                throw new IllegalArgumentException("Latitud fuera de rango: " + hecho.getLatitud());
            }
            hecho.setLatitud(Math.round(hecho.getLatitud() * 1_000_000d) / 1_000_000d);
        }
        if (hecho.getLongitud() != null) {
            if (hecho.getLongitud() < -180 || hecho.getLongitud() > 180) {
                throw new IllegalArgumentException("Longitud fuera de rango: " + hecho.getLongitud());
            }
            hecho.setLongitud(Math.round(hecho.getLongitud() * 1_000_000d) / 1_000_000d);
        }

        // 4. Normalización de fechas
        hecho.setFechaAcontecimiento(validadorFechas.normalizar(hecho.getFechaAcontecimiento()));
        hecho.setFechaCarga(validadorFechas.normalizar(hecho.getFechaCarga()));

        return hecho;
    }

    @Override
    public String normalizarCampo(String campo) {
        return normalizadorLigero.normalizarCampo(campo);
    }

    @Override
    public String normalizarCategoria(String categoria) {
        return mapeadorUbicaciones.normalizar(categoria);
    }

    @Override
    public String normalizarUbicacion(String ubicacion) {
        return mapeadorUbicaciones.normalizar(ubicacion);
    }

    @Override
    public LocalDateTime normalizarFecha(LocalDateTime fecha) {
        return validadorFechas.normalizar(fecha);
    }
}