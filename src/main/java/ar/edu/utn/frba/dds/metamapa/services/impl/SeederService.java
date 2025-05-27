package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.*;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.services.IHechosService;
import ar.edu.utn.frba.dds.metamapa.services.ISeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class SeederService implements ISeederService {

    @Autowired
    private IHechosRepository hechosRepository;


    private HechoBuilder hechoBuilder;

    private Hecho hecho1;
    private Hecho hecho2;
    private Hecho hecho3;
    private Hecho hecho4;
    private Hecho hecho5;
    private Hecho hecho6;


    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public void inicializar() {

       Coordenada coordenada = new Coordenada((float) -34.6037, (float) -58.3816); // Coordenadas de Buenos Aires
       Categoria categoria = new Categoria("Categoria de prueba");
       Contribuyente contribuyente = new Contribuyente("Juan", "Perez", 30, false);

        var hechos = List.of(
                new HechoBuilder("Tsunami Chile", "Descripcion de prueba 1", categoria, coordenada, LocalDateTime.now())
                        .conContribuyente(contribuyente)
                        .conEstado(EstadoHecho.ACEPTADO)
                        .conMultimedia(new Multimedia(baseUrl, "/images/hechos/es-posible-un-tsunami-en-buenos-aires.jpg"))
                        .build(),
                new HechoBuilder("Incendio en el Bolson", "Descripcion de prueba 2", categoria, coordenada, LocalDateTime.now())
                        .conContribuyente(contribuyente)
                        .conEstado(EstadoHecho.ACEPTADO)
                        .conMultimedia(new Multimedia(baseUrl, "/images/hechos/_126544339_fuego12.jpg"))
                        .build(),
                new HechoBuilder("Obvnis en el Cerro Uritorco", "Descripcion de prueba 3", categoria, coordenada, LocalDateTime.now())
                        .conContribuyente(contribuyente)
                        .conEstado(EstadoHecho.RECHAZADO)
                        .conMultimedia(new Multimedia(baseUrl, "/images/hechos/artworks-000351288021-i1m1am-t500x500.jpg"))
                        .build()



        );
        hechos.forEach(hechosRepository::save);

    }

}