package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.*;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.services.IHechosService;
import ar.edu.utn.frba.dds.metamapa.services.ISeederService;
import ar.edu.utn.frba.dds.metamapa.services.ISeederServiceDinamica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeederServiceFuentesDinamicas implements ISeederServiceDinamica {


    @Autowired
    private IHechosRepository hechosRepository;


    private HechoBuilder hechoBuilder;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public void initDinamicas() {

        //Coordenada coordenada = new Coordenada((float) -34.6037, (float) -58.3816); // Coordenadas de Buenos Aires
        //Categoria categoria = new Categoria("Categoria de prueba");
        Contribuyente contribuyente = new Contribuyente("Juan", "Perez", 30, false);

        var hechos = List.of(
                new HechoBuilder("Tsunami Chile", "Descripcion de prueba 1", "Geologico" , -34.6037, -58.3816, LocalDateTime.of(2010, 2, 27, 4, 00))
                        .conContribuyente(contribuyente)
                        .conEstado(EstadoHecho.ACEPTADO)
                        .conMultimedia(new Multimedia(baseUrl, "/images/hechos/es-posible-un-tsunami-en-buenos-aires.jpg"))
                        .build(),
                new HechoBuilder("Incendio en el Bolson", "Descripcion de prueba 2", "Natural", -39.8143, -51.4446,LocalDateTime.of(2019, 11, 5, 14, 30))
                        .conContribuyente(contribuyente)
                        .conEstado(EstadoHecho.ACEPTADO)
                        .conMultimedia(new Multimedia(baseUrl, "/images/hechos/_126544339_fuego12.jpg"))
                        .build(),
                new HechoBuilder("Obvnis en el Cerro Uritorco", "Descripcion de prueba 3", "Paranormal", -61.8413, -12.3346,LocalDateTime.of(2023, 12, 5, 14, 30))
                        .conContribuyente(contribuyente)
                        .conEstado(EstadoHecho.RECHAZADO)
                        .conMultimedia(new Multimedia(baseUrl, "/images/hechos/artworks-000351288021-i1m1am-t500x500.jpg"))
                        .build()



        );
        hechos.forEach(hechosRepository::save);

    }
}
