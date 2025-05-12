package ar.edu.utn.frba.dds.metamapa.services.impl;
import ar.edu.utn.frba.dds.metamapa.models.dtos.output.ColeccionDTO;
import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.repositories.impl.ColeccionRepository;
import ar.edu.utn.frba.dds.metamapa.services.IColeccionesService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ColeccionesService implements IColeccionesService {

    private ColeccionRepository coleccionRepo;

    private String generarHandleUnico(String baseTitulo) {
        String base = baseTitulo.toLowerCase().replaceAll("[^a-z0-9]", "");
        String candidato = base;
        int i = 1;
        List<String> handlesExistentes = this.coleccionRepo.findAll().stream().map(c -> c.getHandle()).toList();
        while (handlesExistentes.contains(candidato)) {
            candidato = base + i;
            i++;
        }
        return candidato;
    }
    @Override
    public void crearDesdeDTO(ColeccionDTO coleccionDTO){
        String handle = generarHandleUnico(coleccionDTO.getTitulo());
        coleccionRepo.save(new Coleccion(coleccionDTO.getTitulo(), coleccionDTO.getDescripcion(),handle, null, null));
    }



}
