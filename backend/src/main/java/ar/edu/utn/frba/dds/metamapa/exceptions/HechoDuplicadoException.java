package ar.edu.utn.frba.dds.metamapa.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class HechoDuplicadoException extends RuntimeException {

    public HechoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
