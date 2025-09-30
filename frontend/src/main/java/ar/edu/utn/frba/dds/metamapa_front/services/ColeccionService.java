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
  private MetamapaApiService metamapaApiService;

  public List<ColeccionDTO> getAllColecciones() {
    return metamapaApiService.getAllColecciones();
  }

  public List<HechoDTO> getHechosByHandle(String handle, HechoFiltroDTO filtros, Boolean curado) {
    return metamapaApiService.getHechosByHandle(handle, filtros, curado);
  }
}
