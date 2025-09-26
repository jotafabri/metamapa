package ar.edu.utn.frba.dds.metamapa.services.normalizador;

import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import org.apache.commons.lang3.StringUtils;

public class NormalizadorLigero implements NormalizadorHecho {



    @Override
    public Hecho normalizar(Hecho hecho) {
        hecho.setCategoria(normalizarCampo(hecho.getCategoria()));
        hecho.setTitulo(normalizarCampo(hecho.getTitulo()));
        return hecho;
    }

    @Override
    public String normalizarCampo(String campo) {
        if (campo == null || campo.isBlank()) return campo;
        return limpiarYCapitalizar(campo);
    }

    @Override
    public String normalizarCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) return categoria;
        return limpiarYCapitalizar(categoria);
    }

        private String limpiarYCapitalizar(String texto) {
            if (texto == null || texto.isBlank()) return texto;
            texto = texto.trim();
            texto = texto.toLowerCase();
            texto = texto.replaceAll("[^\\p{L}\\p{N} ]", "");
            texto = texto.replaceAll("\\s+", " ");
            texto = StringUtils.stripAccents(texto);
            return texto.substring(0,1).toUpperCase() + texto.substring(1);
        }

}
