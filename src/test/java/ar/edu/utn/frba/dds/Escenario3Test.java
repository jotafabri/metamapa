package ar.edu.utn.frba.dds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Escenario3Test {
    private FuenteDinamica fuenteDinamica = new FuenteDinamica();
    private List<Hecho> hechosAgregar = new ArrayList();

    @BeforeEach
    public void init(){
        hechosAgregar.add(new Hecho("Caída de aeronave impacta en Olavarría","Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
                "Caída de aeronave",new Coordenadas((float)-36.868375, (float)-60.343297),
                LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(), Origen.cargaManual));
        hechosAgregar.add(new Hecho("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén","Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
                "Accidente con maquinaria industrial",new Coordenadas((float)-37.345571, (float)-70.241485),
                LocalDate.parse("16/08/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(), Origen.cargaManual));
        hechosAgregar.add(new Hecho("Caída de aeronave impacta en Venado Tuerto, Santa Fe","Grave caída de aeronave ocurrió en las inmediaciones de Venado Tuerto, Santa Fe. El incidente destruyó viviendas y dejó a familias evacuadas. Autoridades nacionales se han puesto a disposición para brindar asistencia.",
                "Caída de aeronave",new Coordenadas((float)-33.768051, (float)-61.921032),
                LocalDate.parse("08/08/2008", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(), Origen.cargaManual));
        hechosAgregar.add(new Hecho("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires","Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.",
                "Accidente en paso a nivel",new Coordenadas((float)-35.855811, (float)-61.940589),
                LocalDate.parse("27/01/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(), Origen.cargaManual));
        hechosAgregar.add(new Hecho("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña","Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.",
                "Derrumbe en obra en construcción",new Coordenadas((float)-26.780008, (float)-60.458782),
                LocalDate.parse("04/06/2016", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(), Origen.cargaManual));
        hechosAgregar.add(new Hecho("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe","Grave brote de enfermedad contagiosa ocurrió en las inmediaciones de San Lorenzo, Santa Fe. El incidente dejó varios heridos y daños materiales. Se ha declarado estado de emergencia en la región para facilitar la asistencia.",
                "Evento sanitario",new Coordenadas((float)-32.786098, (float)-60.741543),
                LocalDate.parse("05/07/2005", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(), Origen.cargaManual));
        for(Hecho hecho: hechosAgregar){
            fuenteDinamica.agregarHecho(hecho);
        }
    }
    @Test
    @DisplayName("Escenario 2. Una parte desde el rol de un contribuyente y otra parte del rol de un administrador.")
    public void escenario3(){
        //contribuyente
        fuenteDinamica.generarSolicitudEliminacionPorTitulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe", "Lorem ipsum dolor sit amet consectetur adipiscing elit, turpis arcu magnis himenaeos suspendisse lacus congue vel, netus quam faucibus sociosqu ultricies venenatis. Justo molestie convallis interdum aenean sed enim, quam phasellus egestas libero magna nisi, rutrum auctor dictum habitasse venenatis. Et mauris ut consequat morbi orci torquent potenti accumsan duis, in himenaeos eleifend habitant diam pellentesque class cras, taciti per nisl justo molestie etiam placerat egestas. Ultrices fermentum habitant orci nibh quis hac magna duis aliquam, interdum proin rutrum imperdiet hendrerit porta co.");

        //admin
        fuenteDinamica.listarSolicitudesEliminacion();
        fuenteDinamica.rechazarSolicitudPorTitulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe");
        fuenteDinamica.listarSolicitudesEliminacion();
        Coleccion coleccionPrueba = new Coleccion("Segunda colección prueba", "Esto es otra prueba", fuenteDinamica, null);
        coleccionPrueba.navegar(null);
        Assertions.assertTrue(coleccionPrueba.importarHechos().stream().anyMatch(h -> h.getTitulo().equalsIgnoreCase("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe")),
                "No se encontró un hecho con el título esperado");

        fuenteDinamica.generarSolicitudEliminacionPorTitulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe", "Lorem ipsum dolor sit amet consectetur adipiscing elit, turpis arcu magnis himenaeos suspendisse lacus congue vel, netus quam faucibus sociosqu ultricies venenatis. Justo molestie convallis interdum aenean sed enim, quam phasellus egestas libero magna nisi, rutrum auctor dictum habitasse venenatis. Et mauris ut consequat morbi orci torquent potenti accumsan duis, in himenaeos eleifend habitant diam pellentesque class cras, taciti per nisl justo molestie etiam placerat egestas. Ultrices fermentum habitant orci nibh quis hac magna duis aliquam, interdum proin rutrum imperdiet hendrerit porta co.");
        fuenteDinamica.aceptarSolicitudPorTitulo("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe");
        fuenteDinamica.listarSolicitudesEliminacion();
        coleccionPrueba.navegar(null);
        Assertions.assertFalse(
                coleccionPrueba.importarHechos().stream().anyMatch(h -> h.getTitulo().equalsIgnoreCase("Brote de enfermedad contagiosa causa estragos en San Lorenzo, Santa Fe")),
                "Se encontró un hecho con un título no deseado"
        );

    }
}
