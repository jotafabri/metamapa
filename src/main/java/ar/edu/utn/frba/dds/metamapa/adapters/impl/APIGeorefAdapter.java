package ar.edu.utn.frba.dds.metamapa.adapters.impl;

import ar.edu.utn.frba.dds.metamapa.adapters.IGeorreferenciacionAdapter;
import ar.edu.utn.frba.dds.metamapa.external.APIGeoref;
import org.springframework.stereotype.Service;

@Service
public class APIGeorefAdapter implements IGeorreferenciacionAdapter {
    private APIGeoref apiGeoref;
    @Override
    public String getNombreProvincia(Double latitud, Double longitud) {
        return this.apiGeoref.getNombreProvincia(latitud, longitud);
    }
}
