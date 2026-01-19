package ar.edu.utn.frba.dds.metamapa.services.normalizador;

import ar.edu.utn.frba.dds.metamapa.services.normalizador.MapeadorTexto;
import ar.edu.utn.frba.dds.metamapa.services.normalizador.ValidadorFechas;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class NormalizadorConfig {

    @Bean
    public MapeadorTexto mapeadorUbicaciones() throws IOException {
        return new MapeadorTexto("static/json/diccionario_ubicaciones.json");
    }

    @Bean
    public MapeadorTexto mapeadorCategorias() throws IOException {
        return new MapeadorTexto("static/json/diccionario_categorias.json");
    }

    @Bean
    public ValidadorFechas validadorFechas() {
        return new ValidadorFechas();
    }
}