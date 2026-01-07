package ar.edu.utn.frba.dds.metamapa.services.normalizador;

import org.springframework.stereotype.Service;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import org.springframework.stereotype.Service;

@Service
public class NormalizadorLigero implements NormalizadorHecho {

    @Override
    public Hecho normalizar(Hecho hecho) {
        hecho.setTitulo(normalizarTitulo(hecho.getTitulo()));
        hecho.setDescripcion(normalizarDescripcion(hecho.getDescripcion()));
        hecho.setCategoria(normalizarCampo(hecho.getCategoria()));
        return hecho;
    }

    @Override
    public String normalizarCampo(String campo) {
        if (campo == null || campo.isBlank()) return campo;
        return capitalizar(campo.toLowerCase().trim());
    }

    public String normalizarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) return titulo;

        // Req 1 y 2: Eliminar emojis Y todos los signos de puntuación (deja solo letras, números y espacios)
        // Usamos [^\\p{L}\\p{N}\\s] para quitar todo lo que no sea Letra, Número o Espacio
        String limpio = titulo.replaceAll("[^\\p{L}\\p{N}\\s]", "");

        // Unificar espacios y pasar a minúsculas
        limpio = limpio.trim().replaceAll("\\s+", " ").toLowerCase();

        // Capitalizar cada palabra (Title Case)
        String[] palabras = limpio.split(" ");
        StringBuilder resultado = new StringBuilder();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                resultado.append(Character.toUpperCase(palabra.charAt(0)))
                        .append(palabra.substring(1)).append(" ");
            }
        }
        return resultado.toString().trim();
    }

    public String normalizarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) return descripcion;

        // Req 1: Eliminar emojis (caracteres no asignados a texto/puntuación estándar)
        // Mantenemos letras, números, puntuación y espacios
        String sinEmojis = descripcion.replaceAll("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", "");

        // Req 3: Reemplazar ! y ? por punto
        String conPuntos = sinEmojis.replaceAll("[!?]", ".");

        // Limpiar espacios duplicados
        String textoLimpio = conPuntos.trim().replaceAll("\\s+", " ");

        // Req 4: Capitalizar oraciones
        return capitalizarOraciones(textoLimpio);
    }

    private String capitalizarOraciones(String texto) {
        // Separamos por puntos
        String[] oraciones = texto.split("\\.");
        StringBuilder resultado = new StringBuilder();

        for (String oracion : oraciones) {
            oracion = oracion.trim();
            if (!oracion.isEmpty()) {
                // Primera letra mayúscula, el resto como venga (o toLowerCase si quieres normalizar todo)
                // Usamos toLowerCase() para que "NATURAL" pase a "natural"
                String capitalizada = Character.toUpperCase(oracion.charAt(0)) + oracion.substring(1).toLowerCase();
                resultado.append(capitalizada).append(". ");
            }
        }
        return resultado.toString().trim();
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}