package ar.edu.utn.frba.dds.metamapa.controllers;

import ar.edu.utn.frba.dds.metamapa.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.metamapa.services.IHechosService;
import ar.edu.utn.frba.dds.metamapa.services.ISeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hechos")
//TODO: o "/api/hechos"
@CrossOrigin(origins = "http://localhost:3000") //TODO:Para probar:
public class hechosController {

    @Autowired
    private ISeederService seederService;

    @Autowired
    private IHechosService hechosService;

    @GetMapping
    public List<HechoOutputDTO> buscarTodosLosHechos() {
        return this.hechosService.buscarTodos();

    }
    @GetMapping("/inicializar")
    public Boolean inicializar(){
        this.seederService.inicializar();
        return true;
    }



}
