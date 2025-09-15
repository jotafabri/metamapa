package ar.edu.utn.frba.dds.metamapa.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class MapeadorTexto {

    private Map<String, String> diccionario;

    public MapeadorTexto(String rutaArchivoJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        InputStream is = getClass().getClassLoader().getResourceAsStream(rutaArchivoJson);
        if (is == null) {
            throw new IOException("No se encontró el recurso: " + rutaArchivoJson);
        }

        diccionario = mapper.readValue(is, Map.class);
    }

    /**
     * Normaliza el texto reemplazando todas las ocurrencias de claves encontradas en el diccionario
     */
    public String normalizar(String texto) {
        if (texto == null || texto.isBlank()) return texto;

        String textoNormalizado = texto;

        // Iterar todas las claves del diccionario y reemplazarlas por su valor
        for (Map.Entry<String, String> entry : diccionario.entrySet()) {
            String clave = entry.getKey().toLowerCase().trim();
            String valor = entry.getValue();

            // Reemplaza todas las ocurrencias ignorando mayúsculas/minúsculas
            textoNormalizado = textoNormalizado.replaceAll("(?i)\\b" + clave + "\\b", valor);
        }

        return textoNormalizado;
    }
}
