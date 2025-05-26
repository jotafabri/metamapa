package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
public class HechoBuilder {


    private static final int LIMITE_DIAS_EDICION_DEFAULT;
    private LocalDateTime fechaCarga = LocalDateTime.now();

    static {
        ResourceBundle config = ResourceBundle.getBundle("application");
        LIMITE_DIAS_EDICION_DEFAULT = Integer.parseInt(config.getString("limite.dias.edicion"));
    }

    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private LocalDateTime fechaAcontecimiento;
    private Multimedia multimedia;
    private Contribuyente contribuyente;
    private List<String> etiquetas = new ArrayList<>();
    private int limiteDiasEdicion = LIMITE_DIAS_EDICION_DEFAULT;

    public HechoBuilder(String titulo, String descripcion, String categoria, Double latitud, Double longitud, LocalDateTime fechaAcontecimiento) {
        if (titulo == null || descripcion == null || categoria == null || latitud == null || longitud == null || fechaAcontecimiento == null) {
            throw new IllegalArgumentException("Los campos título, descripción, categoría, coordenadas, fechaAcontecimiento son obligatorios.");
        }

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaAcontecimiento = fechaAcontecimiento;
    }

    public HechoBuilder conMultimedia(Multimedia multimedia) {
        this.multimedia = multimedia;
        return this;
    }

    public HechoBuilder conContribuyente(Contribuyente contribuyente) {
        this.contribuyente = contribuyente;
        return this;
    }

    public HechoBuilder agregarEtiqueta(String etiqueta) {
        this.etiquetas.add(etiqueta);
        return this;
    }

    public HechoBuilder conLimiteDiasEdicion(int limiteDias) {
        this.limiteDiasEdicion = limiteDias;
        return this;
    }

    public Hecho build() {
        if (this.contribuyente == null) {
            throw new IllegalStateException("Se requiere un contribuyente.");
        }

        Hecho hecho = new Hecho(titulo, descripcion, categoria, latitud, longitud, fechaAcontecimiento, Origen.CONTRIBUYENTE); // usa el constructor nuevo

        hecho.setMultimedia(multimedia);
        hecho.setContribuyente(contribuyente);
        hecho.setLimiteDiasEdicion(limiteDiasEdicion);
        hecho.getEtiquetas().addAll(etiquetas);

        return hecho;
    }



    public HechoBuilder conFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
        return this;
    }

}
