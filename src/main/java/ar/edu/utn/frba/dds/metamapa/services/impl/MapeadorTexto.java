package ar.edu.utn.frba.dds.metamapa.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MapeadorTexto {
    private Map<String, String> diccionario;

    public MapeadorTexto(String rutaArchivoJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        diccionario = mapper.readValue(new File(rutaArchivoJson), Map.class);
    }

    public String normalizar(String texto) {
        if (texto == null || texto.isBlank()) return texto;
        String clave = texto.trim().toLowerCase();
        return diccionario.getOrDefault(clave, texto);
    }
}
