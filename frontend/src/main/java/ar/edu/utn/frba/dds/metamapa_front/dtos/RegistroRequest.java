package ar.edu.utn.frba.dds.metamapa_front.dtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RegistroRequest {
  private String nombre;
  private String apellido;
  private LocalDate fechaNacimiento;
  private String email;
  private String password;
}