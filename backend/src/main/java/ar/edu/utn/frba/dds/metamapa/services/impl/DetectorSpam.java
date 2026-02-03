package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.services.IDetectorSpam;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class DetectorSpam implements IDetectorSpam {

  private static final int MAX_LONGITUD = 50_000;
  private static final int MIN_LONGITUD = 500;
  private static final int MIN_TOKENS = 5;

  @Override
  public boolean esSpam(String texto) {
    if (texto == null) return true;

    String limpio = texto.trim();

    // Texto vacío o ridículamente corto
    if (limpio.length() < MIN_LONGITUD) return true;

    // Texto excesivamente largo (flood)
    if (limpio.length() > MAX_LONGITUD) return true;

    // Repetición excesiva de un mismo caracter (aaaaaa, !!!!!)
    if (limpio.matches(".*(.)\\1{15,}.*")) return true;

    // Texto con muy pocas palabras
    String[] tokens = limpio.split("\\s+");
    if (tokens.length < MIN_TOKENS) return true;

    // Repetición mecánica consecutiva
    if (repiteConsecutivo(tokens)) return true;

    // Texto compuesto mayormente por los mismos tokens
    if (pocaVariedad(tokens)) return true;

    // Demasiados tokens “ruido”
    if (muchoRuido(tokens)) return true;

    return false;
  }

  // Ej: "hola hola hola hola hola"
  private boolean repiteConsecutivo(String[] tokens) {
    int repeticiones = 1;

    for (int i = 1; i < tokens.length; i++) {
      if (tokens[i].equalsIgnoreCase(tokens[i - 1])) {
        repeticiones++;
        if (repeticiones >= 4) {
          return true;
        }
      } else {
        repeticiones = 1;
      }
    }
    return false;
  }

  // Ej: "abc def abc def abc def abc def"
  private boolean pocaVariedad(String[] tokens) {
    Map<String, Integer> contador = new HashMap<>();

    for (String token : tokens) {
      String normalizado = token.toLowerCase();
      contador.put(normalizado, contador.getOrDefault(normalizado, 0) + 1);
    }

    int maxRepeticiones = contador.values()
            .stream()
            .max(Integer::compareTo)
            .orElse(0);

    double proporcion = (double) maxRepeticiones / tokens.length;

    // Si más del 35% del texto es la misma palabra → spam
    return proporcion > 0.35;
  }

  private boolean muchoRuido(String[] tokens) {
    long ruido = Arrays.stream(tokens)
            .filter(t -> t.matches("[a-zA-Z0-9]{1,3}"))
            .count();

    double proporcion = (double) ruido / tokens.length;
    return proporcion > 0.5;
  }



}