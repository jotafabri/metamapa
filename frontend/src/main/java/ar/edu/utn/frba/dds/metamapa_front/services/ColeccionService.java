package ar.edu.utn.frba.dds.metamapa_front.services;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa_front.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoFiltroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColeccionService {
  @Autowired
  private ColeccionesApiService coleccionesApiService;

  public List<ColeccionDTO> getAllColecciones() {
    return coleccionesApiService.getAllColecciones();
  }

  public List<HechoDTO> getHechosByHandle(String handle, HechoFiltroDTO filtros, Boolean curado) {
    return coleccionesApiService.getHechosByHandle(handle, filtros, curado);
  }
}
