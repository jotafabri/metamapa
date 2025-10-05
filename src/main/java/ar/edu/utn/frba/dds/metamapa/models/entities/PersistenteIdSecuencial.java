package ar.edu.utn.frba.dds.metamapa.models.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class PersistenteIdSecuencial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "persistente_seq")
    @SequenceGenerator(name = "persistente_seq", sequenceName = "persistente_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "fecha_alta")
    private LocalDateTime fechaAlta;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    public PersistenteIdSecuencial() {
        this.fechaAlta = LocalDateTime.now();
    }

    protected void setModificado() {
        this.fechaModificacion = LocalDateTime.now();
    }
}
