package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.services.ISeederService;
import org.springframework.stereotype.Service;

@Service
public class SeederService implements ISeederService {

    private IHechosRepository hechoRepository;



    @Override
    public void inicializar() {


    }
}
