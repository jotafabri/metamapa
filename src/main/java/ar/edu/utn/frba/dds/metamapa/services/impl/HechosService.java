package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HechosService implements IHechosService {
    @Autowired
    private IHechosRepository hechosRepository;


    @Override
    public List<HechoOutputDTO> buscarTodos() {
        List<Hecho> hechos = hechosRepository.findAll();

        return hechos.stream()
                .filter(hecho -> hecho.getEstado() == EstadoHecho.ACEPTADO) // Expongo solo los aseptados
                .map(this::hechosOutputDTO)
                .toList();
    }



    private HechoOutputDTO hechosOutputDTO(Hecho hecho){
        HechoOutputDTO dto = new HechoOutputDTO();
        dto.setId(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria().getNombre());
        dto.setLongitudCoordenada(hecho.getCoordenada().getLongitud());
        dto.setLatitudCoordenada(hecho.getCoordenada().getLatitud());
        dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());

        if (hecho.getMultimedia() != null) {
            dto.setMultimediaURL(hecho.getMultimedia().getUrlCompleta());

        } else {
            dto.setMultimediaURL(null);
        }
        return dto;
    }
}
