package ar.edu.utn.frba.dds;

import java.time.LocalDateTime;

public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria; //esto va a ser una clase
    private Coordenadas coordenadas;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Origen origen;

    public Hecho(String titulo, String descripcion, Categoria categoria, Coordenadas coordenadas, LocalDateTime fechaAcontecimiento, LocalDateTime fechaCarga, Origen origen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.coordenadas = coordenadas;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.origen = origen;
    }

    public String getTitulo() {
        return titulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public Categoria getCategoria() {
        return categoria;
    }
    public LocalDateTime getFechaAcontecimiento() {
        return fechaAcontecimiento;
    }
    // public
    //+ eliminarDeColecciones()
}
