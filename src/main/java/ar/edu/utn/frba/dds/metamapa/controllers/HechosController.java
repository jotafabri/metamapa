package ar.edu.utn.frba.dds.metamapa.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.HechoFiltroDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.services.IHechosService;
import ar.edu.utn.frba.dds.metamapa.services.ISeederService;
import ar.edu.utn.frba.dds.metamapa.services.ISeederServiceDinamica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hechos")

public class HechosController {
  @Autowired
  private IHechosService hechosService;

  @Autowired
  private ISeederService seederService;

  @Autowired
  private ISeederServiceDinamica seederServiceDinamicas;

    /*
    //dinamicos
    @GetMapping("/dinamicos")
    public List<HechoOutputDTO> getHechosAceptadosFiltrados(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String fecha_reporte_desde,
            @RequestParam(required = false) String fecha_reporte_hasta,
            @RequestParam(required = false) String fecha_acontecimiento_desde,
            @RequestParam(required = false) String fecha_acontecimiento_hasta,
            @RequestParam(required = false) String ubicacion
    ) {
        return hechosService.buscarTodos(
                categoria,
                fecha_reporte_desde,
                fecha_reporte_hasta,
                fecha_acontecimiento_desde,
                fecha_acontecimiento_hasta,
                ubicacion);
    }
*/

  @GetMapping
  public List<HechoDTO> getHechosWithParams(
      @ModelAttribute HechoFiltroDTO filtros
  ) {
    return this.hechosService.getHechosWithParams(filtros);
  }

  @GetMapping("/inicializar")
  public boolean inicializarDatos() {
    this.seederService.init();
    //this.seederServiceDinamicas.initDinamicas();
    return true;
  }
}