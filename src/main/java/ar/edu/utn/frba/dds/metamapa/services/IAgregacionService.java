package ar.edu.utn.frba.dds.metamapa.services;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;

import java.util.List;

public interface IAgregacionService {

    public void refrescarColecciones();
    List<Coleccion> obtenerColecciones();

}
