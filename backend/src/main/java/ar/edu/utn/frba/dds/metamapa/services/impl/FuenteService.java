package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteProxy;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IFuentesRepository;
import ar.edu.utn.frba.dds.metamapa.services.IFuenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuenteService implements IFuenteService {

    @Autowired
    private IFuentesRepository fuentesRepository;

    @Override
    public List<Fuente> listarFuentes() {
        return fuentesRepository.findAll();
    }

    @Override
    public Fuente mostrarFuente(Long id) {
        return fuentesRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Fuente no encontrada con ID: " + id)
        );
    }

    @Override
    public Fuente crearFuente(FuenteInputDTO dto) {
        Fuente fuente;
        switch (dto.getTipo().toUpperCase()) {
            case "ESTATICA" -> fuente = new FuenteEstatica(dto.getRuta());
            case "DINAMICA" -> fuente = new FuenteDinamica();
            case "PROXY" -> fuente = new FuenteProxy(dto.getRuta());
            default -> throw new IllegalArgumentException("Tipo de fuente no válido: " + dto.getTipo());
        }
        if (dto.getTitulo() != null && !dto.getTitulo().isEmpty()) {
            fuente.setTitulo(dto.getTitulo());
        }
        return fuentesRepository.save(fuente);
    }

    @Override
    public void eliminarFuente(Long id) {
        fuentesRepository.deleteById(id);
    }
}