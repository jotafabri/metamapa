package ar.edu.utn.frba.dds.metamapa.services.impl;

import ar.edu.utn.frba.dds.metamapa.models.entities.*;
import ar.edu.utn.frba.dds.metamapa.models.repositories.impl.ColeccionRepository;
import ar.edu.utn.frba.dds.metamapa.services.IAgregacionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgregacionService implements IAgregacionService {

    private final ColeccionRepository coleccionRepo;

    private AgregacionService(ColeccionRepository coleccionRepo) {
        this.coleccionRepo = coleccionRepo;
    }
    //private final HechoRepository hechoRepo;

    @Override
    public void refrescarColecciones(){
        for(Coleccion coleccion : this.obtenerColecciones()){
            refrescarHechosColeccion(coleccion);
        }
    }
    //TODO
    public List<Coleccion> obtenerColecciones() {
        return coleccionRepo.findAll();
    }

    public void refrescarHechosColeccion(Coleccion coleccion) {
        Fuente fuente = coleccion.getFuente();

        List<Hecho> hechosActualizados = fuente.getListaHechos();

        List<Hecho> hechosFiltrados = new ArrayList<>();
        for (Hecho hecho : hechosActualizados) {
            boolean cumpleTodos = coleccion.getCriterios().stream()
                    .allMatch(criterio -> criterio.cumple(hecho));
            if (cumpleTodos || coleccion.getCriterios().isEmpty()) {
                hechosFiltrados.add(hecho);
            }
        }

        for (Hecho hecho : hechosFiltrados) {
            if (fuente instanceof FuenteDinamica fuenteDinamica) {
                boolean fueEliminado = fuenteDinamica.getSolicitudesEliminacion().stream()
                        .anyMatch(s -> s.getEstado().equals(Estado.ACEPTADA)
                                && s.getHecho().getTitulo().equalsIgnoreCase(hecho.getTitulo()));
                if (fueEliminado) continue;

                fuenteDinamica.agregarHecho(hecho);
            }

            if (fuente instanceof FuenteEstatica fuenteEstatica) {
                boolean yaExiste = fuenteEstatica.getListaHechos().stream()
                        .anyMatch(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()));
                if (!yaExiste) {
                    fuenteEstatica.getListaHechos().add(hecho);
                } else {
                    fuenteEstatica.getListaHechos().removeIf(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()));
                    fuenteEstatica.getListaHechos().add(hecho);
                }
            }
        }

        System.out.println("Refrescada la colección: " + coleccion.getTitulo());
    }




}

