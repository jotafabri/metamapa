package ar.edu.utn.frba.dds.metamapa;

import ar.edu.utn.frba.dds.metamapa.models.entities.Coleccion;
import ar.edu.utn.frba.dds.metamapa.models.entities.Coordenadas;
import ar.edu.utn.frba.dds.metamapa.models.entities.CriterioCategoria;
import ar.edu.utn.frba.dds.metamapa.models.entities.CriterioFechaAcontecimiento;
import ar.edu.utn.frba.dds.metamapa.models.entities.CriterioPertenencia;
import ar.edu.utn.frba.dds.metamapa.models.entities.CriterioTitulo;
import ar.edu.utn.frba.dds.metamapa.models.entities.FuenteDinamica;
import ar.edu.utn.frba.dds.metamapa.models.entities.Hecho;
import ar.edu.utn.frba.dds.metamapa.models.entities.Origen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Escenario1Test {
    private FuenteDinamica fuenteDinamica = new FuenteDinamica();
    private List<Hecho> hechosAgregar = new ArrayList();

    @BeforeEach
    public void init(){
        hechosAgregar.add(new Hecho("Caída de aeronave impacta en Olavarría","Grave caída de aeronave ocurrió en las inmediaciones de Olavarría, Buenos Aires. El incidente provocó pánico entre los residentes locales. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
                "Caída de aeronave",new Coordenadas((float)-36.868375, (float)-60.343297),
                LocalDate.parse("29/11/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(), Origen.CARGA_MANUAL));
        hechosAgregar.add(new Hecho("Serio incidente: Accidente con maquinaria industrial en Chos Malal, Neuquén","Un grave accidente con maquinaria industrial se registró en Chos Malal, Neuquén. El incidente dejó a varios sectores sin comunicación. Voluntarios de diversas organizaciones se han sumado a las tareas de auxilio.",
                "Accidente con maquinaria industrial",new Coordenadas((float)-37.345571, (float)-70.241485),
                LocalDate.parse("16/08/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(), Origen.CARGA_MANUAL));
        hechosAgregar.add(new Hecho("Caída de aeronave impacta en Venado Tuerto, Santa Fe","Grave caída de aeronave ocurrió en las inmediaciones de Venado Tuerto, Santa Fe. El incidente destruyó viviendas y dejó a familias evacuadas. Autoridades nacionales se han puesto a disposición para brindar asistencia.",
                "Caída de aeronave",new Coordenadas((float)-33.768051, (float)-61.921032),
                LocalDate.parse("08/08/2008", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(), Origen.CARGA_MANUAL));
        hechosAgregar.add(new Hecho("Accidente en paso a nivel deja múltiples daños en Pehuajó, Buenos Aires","Grave accidente en paso a nivel ocurrió en las inmediaciones de Pehuajó, Buenos Aires. El incidente generó preocupación entre las autoridades provinciales. El Ministerio de Desarrollo Social está brindando apoyo a los damnificados.",
                "Accidente en paso a nivel",new Coordenadas((float)-35.855811, (float)-61.940589),
                LocalDate.parse("27/01/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(), Origen.CARGA_MANUAL));
        hechosAgregar.add(new Hecho("Devastador Derrumbe en obra en construcción afecta a Presidencia Roque Sáenz Peña","Un grave derrumbe en obra en construcción se registró en Presidencia Roque Sáenz Peña, Chaco. El incidente generó preocupación entre las autoridades provinciales. El intendente local se ha trasladado al lugar para supervisar las operaciones.",
                "Derrumbe en obra en construcción",new Coordenadas((float)-26.780008, (float)-60.458782),
                LocalDate.parse("04/06/2016", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(), Origen.CARGA_MANUAL));
        for(Hecho hecho: hechosAgregar){
            fuenteDinamica.agregarHecho(hecho);
        }
    }

    @Test
    @DisplayName("Escenario 1. Desde el rol del administrador")
    public void escenario1() {
        Coleccion coleccionPrueba = new Coleccion("Colección prueba", "Esto es una prueba", null, null);
        coleccionPrueba.setFuente(fuenteDinamica);
        Assertions.assertEquals(5,coleccionPrueba.darHechos().size());

        var listaVacia = new ArrayList<CriterioPertenencia>();
        var lista = coleccionPrueba.navegar(listaVacia);
        Assertions.assertEquals(5,lista.size());

        // Ahora le agrego criterios
        coleccionPrueba.agregarCriterio(new CriterioFechaAcontecimiento(LocalDate.parse("01/01/2000", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay(), LocalDate.parse("01/01/2010", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay()));
        Assertions.assertEquals(3, coleccionPrueba.darHechos().size());

//        coleccionPrueba.navegar(null);
//        Assertions.assertEquals(3, coleccionPrueba.darHechos().size());
//        System.out.println("Ahora le agrego un criterio más");
//        coleccionPrueba.agregarCriterio(new CriterioCategoria("Caída de aeronave"));
//        coleccionPrueba.navegar(null);
//        Assertions.assertEquals(2, coleccionPrueba.darHechos().size());
//        System.out.println("Ahora busco con filtros y da vacío");

        // Busco con filtros y debería dar vacío
        CriterioPertenencia[] array = {new CriterioCategoria("Caída de Aeronave"),new CriterioTitulo("un título")};
        lista = coleccionPrueba.navegar(Arrays.asList(array));
        Assertions.assertTrue(lista.isEmpty());

        // Busco con un filtro que se que existe
        CriterioPertenencia[] filtro = {new CriterioCategoria("Caída de aeronave")};
        lista = coleccionPrueba.navegar(Arrays.asList(filtro));
        Assertions.assertEquals(2,lista.size());
//        System.out.println("Como bien se pudo ver, no mostró nada por pantalla.");
//        System.out.println("Ahora lo etiqueto");
//        fuenteDinamica.etiquetarPorTitulo("Caída de aeronave impacta en Olavarría", "Olavarría");
//        fuenteDinamica.etiquetarPorTitulo("Caída de aeronave impacta en Olavarría", "Grave");
//        Assertions.assertEquals(2, fuenteDinamica.buscarHechoPorTitulo("Caída de aeronave impacta en Olavarría").getEtiquetas().size());
//        fuenteDinamica.buscarHechoPorTitulo("Caída de aeronave impacta en Olavarría").printEtiquetas();
    }
}
