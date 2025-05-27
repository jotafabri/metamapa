package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.ListaDeCriterios;
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
  public List<HechoDTO> getHechosWithParams(String categoria,
                                            String fecha_reporte_desde,
                                            String fecha_reporte_hasta,
                                            String fecha_acontecimiento_desde,
                                            String fecha_acontecimiento_hasta,
                                            String ubicacion) {
    var filtro = new ListaDeCriterios().getListFromParams(categoria,
        fecha_reporte_desde,
        fecha_reporte_hasta,
        fecha_acontecimiento_desde,
        fecha_acontecimiento_hasta,
        ubicacion);

    return this.hechosRepository.findAll()
        .stream()
        .filter(h -> filtro.stream().allMatch(c -> c.cumple(h)))
        .map(HechoDTO::fromHecho)
        .toList();
  }


  //TODO:agregue estos dos metodos de fuente dinamica
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
        dto.setCategoria(hecho.getCategoria());
        dto.setLongitudCoordenada(hecho.getLongitud());
        dto.setLatitudCoordenada(hecho.getLatitud());
        dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());

        if (hecho.getMultimedia() != null) {
            dto.setMultimediaURL(hecho.getMultimedia().getUrlCompleta());

        } else {
            dto.setMultimediaURL(null);
        }
        return dto;
    }
}
