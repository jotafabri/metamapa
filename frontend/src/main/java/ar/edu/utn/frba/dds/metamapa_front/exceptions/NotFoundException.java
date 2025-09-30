package ar.edu.utn.frba.dds.metamapa_front.exceptions;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String entidad, String id) {
    super("No se ha encontrado " + entidad + " de id " + id);
  }
}
