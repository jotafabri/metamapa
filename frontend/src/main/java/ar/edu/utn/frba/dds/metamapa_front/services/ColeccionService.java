package ar.edu.utn.frba.dds.metamapa_front.services;

import java.util.List;
import java.util.Optional;

import ar.edu.utn.frba.dds.metamapa_front.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColeccionService {
  @Autowired
  private ColeccionesApiService coleccionesApiService;

  public List<ColeccionDTO> getAllColecciones() {
    return coleccionesApiService.getAllColecciones();
  }

  public List<HechoDTO> getHechosByHandle(String handle) {
    return coleccionesApiService.getHechosByHandle(handle);
  }
}
