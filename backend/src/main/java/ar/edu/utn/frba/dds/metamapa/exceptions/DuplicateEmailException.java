package ar.edu.utn.frba.dds.metamapa.exceptions;

public class DuplicateEmailException extends RuntimeException {
  public DuplicateEmailException(String email) {
    super("El email " + email + " ya está registrado");
  }
}
