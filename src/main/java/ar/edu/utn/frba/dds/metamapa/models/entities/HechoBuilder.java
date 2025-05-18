package ar.edu.utn.frba.dds.metamapa.models.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HechoBuilder {


    private static final boolean VISIBLE_DEFAULT;
    private static final int LIMITE_DIAS_EDICION_DEFAULT;

    static {
        ResourceBundle config = ResourceBundle.getBundle("application");
        VISIBLE_DEFAULT = Boolean.parseBoolean(config.getString("hecho.visible"));
        LIMITE_DIAS_EDICION_DEFAULT = Integer.parseInt(config.getString("limite.dias.edicion"));
    }

    private String titulo;
    private String descripcion;
    private String categoria;
    private Coordenadas coordenadas;
    private LocalDateTime fechaAcontecimiento;
    private Origen origen;
    private Multimedia multimedia;
    private Contribuyente contribuyente;
    private List<String> etiquetas = new ArrayList<>();
    private boolean visible = VISIBLE_DEFAULT;
    private int limiteDiasEdicion = LIMITE_DIAS_EDICION_DEFAULT;

    public HechoBuilder(String titulo, String descripcion, String categoria, Coordenadas coordenadas, LocalDateTime fechaAcontecimiento, Origen origen) {
        if (titulo == null || descripcion == null || categoria == null || coordenadas == null || fechaAcontecimiento == null || origen == null) {
            throw new IllegalArgumentException("Los campos título, descripción, categoría, coordenadas, fechaAcontecimiento y origen son obligatorios.");
        }

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.coordenadas = coordenadas;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.origen = origen;
    }

    public HechoBuilder conMultimedia(Multimedia multimedia) {
        this.multimedia = multimedia;
        return this;
    }

    public HechoBuilder conContribuyente(Contribuyente contribuyente) {
        if (this.origen != Origen.CONTRIBUYENTE) {
            throw new IllegalStateException("Solo se pueden agregar contribuyentes a hechos con origen CONTRIBUYENTE.");
        }
        this.contribuyente = contribuyente;
        return this;
    }

    public HechoBuilder agregarEtiqueta(String etiqueta) {
        this.etiquetas.add(etiqueta);
        return this;
    }

    public HechoBuilder visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public HechoBuilder conLimiteDiasEdicion(int limiteDias) {
        this.limiteDiasEdicion = limiteDias;
        return this;
    }

    public Hecho build() {
        if (this.origen == Origen.CONTRIBUYENTE && this.contribuyente == null) {
            throw new IllegalStateException("Los hechos de tipo CONTRIBUYENTE requieren un contribuyente.");
        }

        Hecho hecho = new Hecho();
        hecho.setTitulo(titulo);
        hecho.setDescripcion(descripcion);
        hecho.setCategoria(categoria);
        hecho.setCoordenadas(coordenadas);
        hecho.setFechaAcontecimiento(fechaAcontecimiento);
        hecho.setOrigen(origen);
        hecho.setMultimedia(multimedia);
        hecho.setContribuyente(contribuyente);
        hecho.setVisible(visible);
        hecho.setLimiteDiasEdicion(limiteDiasEdicion);
        hecho.getEtiquetas().addAll(etiquetas);

        return hecho;
    }
}
