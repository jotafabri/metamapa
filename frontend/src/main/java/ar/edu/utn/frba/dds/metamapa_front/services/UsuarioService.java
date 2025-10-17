package ar.edu.utn.frba.dds.metamapa_front.services;

import ar.edu.utn.frba.dds.metamapa_front.dtos.UsuarioDTO;
import ar.edu.utn.frba.dds.metamapa_front.dtos.AuthResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private MetamapaApiService metamapaApiService;

    public void crearUsuario(UsuarioDTO usuarioDTO) {
        metamapaApiService.crearUsuario(usuarioDTO);
    }

    public AuthResponseDTO autenticar(UsuarioDTO usuarioDTO) {
        return metamapaApiService.login(usuarioDTO.getEmail(), usuarioDTO.getPassword());
    }
}
