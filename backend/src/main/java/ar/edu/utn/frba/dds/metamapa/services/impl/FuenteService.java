package ar.edu.utn.frba.dds.metamapa.services.impl;

import java.util.List;

import ar.edu.utn.frba.dds.metamapa.models.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.enums.Origen;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.Fuente;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteDinamica;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.metamapa.models.entities.fuentes.FuenteProxy;
import ar.edu.utn.frba.dds.metamapa.models.entities.hechos.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.utils.LectorCSV;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IFuentesRepository;
import ar.edu.utn.frba.dds.metamapa.models.repositories.IHechosRepository;
import ar.edu.utn.frba.dds.metamapa.services.IFuenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FuenteService implements IFuenteService {

    @Autowired
    private IFuentesRepository fuentesRepository;

    @Autowired
    private IHechosRepository hechosRepository;

    @Override
    public List<Fuente> listarFuentes() {
        return fuentesRepository.findAll();
    }

    @Override
    public Fuente mostrarFuente(Long id) {
        return fuentesRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Fuente no encontrada con ID: " + id)
        );
    }

    @Override
    @Transactional
    public Fuente crearFuente(FuenteInputDTO dto) {
        Fuente fuente;
        switch (dto.getTipo().toUpperCase()) {
            case "ESTATICA" -> {
                fuente = new FuenteEstatica(dto.getRuta());
                // Cargar hechos ANTES de guardar la fuente
                if (dto.getRuta() != null && !dto.getRuta().isEmpty()) {
                    List<Hecho> hechos = new LectorCSV().leer(dto.getRuta());
                    for (Hecho hecho : hechos) {
                        hecho.aceptar();
                        hecho.setFuente(fuente);
                        hecho.setOrigen(Origen.DATASET);
                    }
                    // Agregar hechos a la fuente
                    fuente.getHechos().addAll(hechos);
                }
            }
            case "DINAMICA" -> fuente = new FuenteDinamica();
            case "PROXY" -> fuente = new FuenteProxy(dto.getRuta());
            default -> throw new IllegalArgumentException("Tipo de fuente no válido: " + dto.getTipo());
        }
        if (dto.getTitulo() != null && !dto.getTitulo().isEmpty()) {
            fuente.setTitulo(dto.getTitulo());
        }

        System.out.println("DEBUG: Antes de guardar fuente. Tipo: " + fuente.getClass().getSimpleName());
        System.out.println("DEBUG: Hechos en memoria antes de guardar: " + fuente.getHechos().size());

        // Guardar la fuente PRIMERO
        fuente = fuentesRepository.save(fuente);

        System.out.println("DEBUG: Después de guardar fuente. ID: " + fuente.getId());

        // Para fuentes estáticas, persistir los hechos
        if (fuente instanceof FuenteEstatica && !fuente.getHechos().isEmpty()) {
            System.out.println("DEBUG: Guardando " + fuente.getHechos().size() + " hechos...");
            hechosRepository.saveAll(fuente.getHechos());
            System.out.println("DEBUG: Hechos guardados correctamente");
        }

        return fuente;
    }

    @Override
    public void eliminarFuente(Long id) {
        fuentesRepository.deleteById(id);
    }
}