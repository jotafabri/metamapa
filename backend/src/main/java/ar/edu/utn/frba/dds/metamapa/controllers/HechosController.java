package ar.edu.utn.frba.dds.metamapa.controllers;

import java.io.IOException;
import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.services.IFileStorageService;
import ar.edu.utn.frba.dds.metamapa.services.IHechosService;
import ar.edu.utn.frba.dds.metamapa.services.ISeederService;
import ar.edu.utn.frba.dds.metamapa.services.ISeederServiceDinamica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/hechos")
public class HechosController {
  @Autowired
  private IHechosService hechosService;

  @Autowired
  private ISeederService seederService;

  @Autowired
  private IFileStorageService fileStorageService;

  @GetMapping
  public List<HechoDTO> getHechosWithParams(
      @ModelAttribute HechoFiltroDTO filtros
  ) {
    return this.hechosService.getHechosWithParams(filtros);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public HechoDTO crearHecho(
      @RequestPart("hecho") HechoDTO hechoDTO,
      @RequestPart(value = "archivos", required = false) List<MultipartFile> archivos
  ) {
    try {
      if (archivos != null && !archivos.isEmpty()) {
        List<String> nombresGuardados = fileStorageService.guardarMultiples(archivos);
        hechoDTO.setMultimedia(nombresGuardados);
      }
      return hechosService.crearHechoDesdeDTO(hechoDTO);
    } catch (IOException e) {
      return null;
    }
  }

  @GetMapping("/{id}")
  public HechoDTO getHechoById(@PathVariable Long id) {
    return this.hechosService.getHechoById(id);
  }

  @PatchMapping("/{id}")
  public HechoDTO actualizarHecho(@PathVariable Long id, @RequestBody HechoDTO hechoDTO) {
    return this.hechosService.actualizarHecho(id, hechoDTO);
  }

  @GetMapping("/inicializar")
  public boolean inicializarDatos() {
    this.seederService.init();
    //this.seederServiceDinamicas.initDinamicas();
    return true;
  }
}