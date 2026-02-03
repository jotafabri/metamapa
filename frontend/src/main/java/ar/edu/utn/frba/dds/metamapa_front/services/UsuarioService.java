package ar.edu.utn.frba.dds.metamapa_front.services;

import ar.edu.utn.frba.dds.metamapa_front.dtos.AuthResponseDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.LoginRequest;
import ar.edu.utn.frba.dds.metamapa_front.dtos.RegistroRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private MetamapaApiService metamapaApiService;

    public void crearUsuario(RegistroRequest registroRequest) {
        metamapaApiService.crearUsuario(registroRequest);
    }

    public AuthResponseDTO autenticar(LoginRequest loginRequest) {
        return metamapaApiService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }
}
