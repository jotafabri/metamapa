package ar.edu.utn.frba.dds.metamapa_front.services;

import java.util.Optional;

import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HechosService {
  @Autowired
  private MetamapaApiService metamapaApiService;

  public Optional<HechoDTO> getHechoById(Long id) {
    try {
      HechoDTO hecho = metamapaApiService.getHechoById(id);
      return Optional.of(hecho);
    } catch (NotFoundException e) {
      return Optional.empty();
    }
  }

  public HechoDTO crearHecho(HechoDTO hechoDTO) {
    return metamapaApiService.crearHecho(hechoDTO);
  }

  public HechoDTO actualizarHecho(Long id, HechoDTO hechoDTO) {
    return metamapaApiService.actualizarHecho(id, hechoDTO);
  }
}
