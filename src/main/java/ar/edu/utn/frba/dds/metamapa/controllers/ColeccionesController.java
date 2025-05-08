package ar.edu.utn.frba.dds.metamapa.controllers;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.metamapa.services.IColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/colecciones")
public class ColeccionesController {
  @Autowired
  private IColeccionService coleccionService;

  @GetMapping
  public List<ColeccionDTO> getAllColecciones() {
    return this.coleccionService.getAllColecciones();
  }

  @GetMapping("/{id}/hechos")
  public List<HechoDTO> getHechosById(@PathVariable Long id) {
    return coleccionService.getHechosById(id);
  }
}
