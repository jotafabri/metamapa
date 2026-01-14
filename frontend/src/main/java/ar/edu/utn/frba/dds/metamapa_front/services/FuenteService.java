package ar.edu.utn.frba.dds.metamapa_front.services;

import ar.edu.utn.frba.dds.metamapa_front.dtos.FuenteDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.FuenteOutputDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.HechoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FuenteService {

  @Autowired
  private MetamapaApiService metamapaApiService;


  public void crearFuente(FuenteDTO fuenteDTO) {
    metamapaApiService.crearFuente(fuenteDTO);
  }

  public void crearFuenteEstatica(MultipartFile archivo, String titulo) {
    metamapaApiService.crearFuenteEstatica(archivo, titulo);
  }

  public List<FuenteOutputDTO> obtenerTodasLasFuentes()  {
    return metamapaApiService.getTodasLasFuentes();
  }

  public List<HechoDTO> getMisHechos() {
    return metamapaApiService.getMisHechos();
  }
}
